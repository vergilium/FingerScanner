package ua.ks.hogo.fingerscanner.uartdriver.Abstract;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import java.io.IOException;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ISerial {
    int[] SERIAL_BAUDRATES_ALLOVED = {
            SerialPort.BAUDRATE_115200
            ,SerialPort.BAUDRATE_57600
            ,SerialPort.BAUDRATE_38400
            ,SerialPort.BAUDRATE_19200
            ,SerialPort.BAUDRATE_9600};
    int SERIAL_BAUDRATE_DEFAULT = SerialPort.BAUDRATE_115200;

    boolean isOpened();

    void openPort() throws IOException, SerialPortException;
    void closePort() throws IOException, SerialPortException;

    boolean sendPacket(IZKPacket packet) throws SerialPortException;
    IZKPacket getPacket(IZKPacket packet) throws SerialPortException, SerialPortTimeoutException;

    int getTemplate(List<Byte> template, Integer size) throws SerialPortException;
}
