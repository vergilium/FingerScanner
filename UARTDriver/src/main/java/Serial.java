import Abstract.ISerial;

import Abstract.IZKPacket;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;

public final class Serial implements ISerial {
    private static Serial instance;
    private static SerialPort serialPort;
    private final Integer baudrate;
    private final short stopbit;
    private final short parity;
    private final short databits;
    private boolean isOpened;

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
    }

    public boolean isOpened(){
        return isOpened;
    }

    public static ISerial initPort() throws InvalidParameterException {
        if(instance == null){
            instance = new Serial(Settings.getInstance());
        }
        return instance;
    }

    @Override
    public void openPort() throws SerialPortException {
        isOpened = serialPort.openPort();
        serialPort.setParams(baudrate, databits, stopbit, parity);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        int mask = SerialPort.MASK_RXCHAR;
        serialPort.setEventsMask(mask);
        serialPort.addEventListener(new SerialPortReader());
    }

    @Override
    public void closePort() throws SerialPortException {
        serialPort.closePort();
    }

    @Override
    public boolean sendPacket(IZKPacket packet) throws SerialPortException {
        StringBuilder str = new StringBuilder("Sended data: ");
        for(byte b: packet.getPacket()){
            str.append(String.format(" 0x%02x", b));
        }
        System.out.println(str.toString());
        return serialPort.writeBytes(packet.getPacket());
    }

    static class SerialPortReader implements SerialPortEventListener{

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            try {
                byte[] buffer = serialPort.readBytes();

                StringBuilder str = new StringBuilder("Received data: ");
                for(byte b: buffer){
                    str.append(String.format(" 0x%02x", b));
                }
                System.out.println(str.toString());
            }
            catch (SerialPortException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
