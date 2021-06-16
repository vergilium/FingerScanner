import Abstract.ISerial;

import Abstract.IZKPacket;
import jssc.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Serial implements ISerial {
    private static Serial instance;
    private static SerialPort serialPort;
    private final Integer baudrate;
    private final short stopbit;
    private final short parity;
    private final short databits;
    private final int recTimeout;
    private boolean isOpened;

    /**
     * Static method for get serial port instanse.
     * If object of serial do not created, it will be create
     * and return new instance.
     * @return {ISerial} serial port instance
     * @throws InvalidParameterException
     */
    public static ISerial initPort() throws InvalidParameterException {
        if(instance == null){
            instance = new Serial(Settings.getInstance());
        }
        return instance;
    }

    /**
     * COnstructor of serial class.
     * @param config {Settings} object for port configuration
     * @see Settings#getInstance()
     * @throws InvalidParameterException
     */
    private Serial(Settings config) throws InvalidParameterException {
        serialPort = new SerialPort(config.UART_PORT);
        if(Arrays.stream(SERIAL_BAUDRATES_ALLOVED).anyMatch(b -> b == config.UART_BAUDRATE)) {
            baudrate = config.UART_BAUDRATE;
        } else {
            throw new InvalidParameterException("Baudrate parameter has not granted");
        }
        stopbit = config.UART_STOPBIT;
        parity = config.UART_PARITY;
        databits = config.UART_DATABITS;
        isOpened = false;
        recTimeout = config.UART_RECEIVE_TIMEOUT;
    }

    /**
     * Getter for flag of port open status
     * @return {boolean} port status
     */
    @Override
    public boolean isOpened(){
        return isOpened;
    }

    /**
     * Open and configure port.
     * Configuration parameter can set in constructor.
     * @see Serial#Serial(Settings)
     * @throws SerialPortException
     */
    @Override
    public void openPort() throws SerialPortException {
                String[] portNames = SerialPortList.getPortNames();
                for (String portName : portNames) {
                    System.out.println(portName);
                }
        isOpened = serialPort.openPort();
        serialPort.setParams(baudrate, databits, stopbit, parity);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
    }

    @Override
    public void closePort() throws SerialPortException {
        serialPort.closePort();
    }

    @Override
    public IZKPacket getPacket(IZKPacket packet) throws SerialPortException, SerialPortTimeoutException {
         return packet.receivePacket(serialPort.readBytes(IZKPacket.PACKET_SIZE, recTimeout));
    }

    @Override
    public boolean sendPacket(IZKPacket packet) throws SerialPortException {
        StringBuilder str = new StringBuilder("Sended data: ");
        for(byte b: packet.makePacket()){
            str.append(String.format(" 0x%02x", b));
        }
       // System.out.println(str.toString());
        return serialPort.writeBytes(packet.makePacket());
    }

    @Override
    public int getTemplate(List<Byte> template, Integer size) throws SerialPortException {
        for(byte b: serialPort.readBytes(size)){
            template.add(b);
        }
        return template.size();
    }

}
