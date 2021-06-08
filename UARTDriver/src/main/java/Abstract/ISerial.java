package Abstract;

import Events.EventManager;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.IOException;
import java.security.InvalidParameterException;

public interface ISerial {
    int[] SERIAL_BAUDRATES_ALLOVED = {
            SerialPort.BAUDRATE_115200,
            SerialPort.BAUDRATE_57600,
            SerialPort.BAUDRATE_38400,
            SerialPort.BAUDRATE_19200,
            SerialPort.BAUDRATE_9600,};
    int SERIAL_BAUDRATE_DEFAULT = SerialPort.BAUDRATE_115200;
    /*int SERIAL_STOPBIT_DEFAULT = SerialPort.STOPBITS_1;
    int SERIAL_PARITY_DEFAULT = SerialPort.PARITY_NONE;
    int SERIAL_DATASIZE_DEFAULT = SerialPort.DATABITS_8; */
    EventManager events = null;

    boolean isOpened();

    //ISerial initPort() throws InvalidParameterException;
    void openPort() throws IOException, SerialPortException;
    void closePort() throws IOException, SerialPortException;

    boolean sendPacket(IZKPacket packet) throws SerialPortException;
}
