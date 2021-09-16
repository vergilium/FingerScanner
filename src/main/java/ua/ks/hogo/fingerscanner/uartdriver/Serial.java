package ua.ks.hogo.fingerscanner.uartdriver;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ua.ks.hogo.fingerscanner.config.UARTConfig;
import ua.ks.hogo.fingerscanner.uartdriver.Abstract.*;
import jssc.*;

import java.security.InvalidParameterException;
import java.util.List;

@Component
@EnableConfigurationProperties(UARTConfig.class)
@Log4j2
@SuppressWarnings("unused")
public final class Serial implements ISerial, DisposableBean {
    private final UARTConfig serialConf;

    private static SerialPort serialPort;
    private boolean isOpened;

    /**
     * COnstructor of serial class.
     */
    Serial(UARTConfig config) throws InvalidParameterException {
        this.serialConf = config;
        serialPort = new SerialPort(serialConf.getPort());
        isOpened = false;
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
     * @see Serial#Serial(UARTConfig)
     * @throws SerialPortException -
     */
    @Override
    public void openPort() throws SerialPortException {
        log.debug("Serial port parameters: " +
                " Baudrate: " + serialConf.getBaudrate() +
                " Databits: " + serialConf.getDatabits() +
                " Stop bits: " + serialConf.getStopbit() +
                " Parity: " + serialConf.getParity());
        isOpened = serialPort.openPort();
        if(isOpened){
            log.info("Serial port has been opened!");
        } else {
            log.warn("Serial port not opened!");
        }
        serialPort.setParams(serialConf.getBaudrate()
                ,serialConf.getDatabits()
                ,serialConf.getStopbit()
                ,serialConf.getParity());
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
    }

    @Override
    public void closePort() throws SerialPortException {
        serialPort.closePort();
    }

    @Override
    public IZKPacket getPacket(IZKPacket packet) throws SerialPortException, SerialPortTimeoutException {
         IZKPacket pkt = packet.receivePacket(serialPort.readBytes(IZKPacket.PACKET_SIZE, serialConf.getRecTimeout()));
         log.debug("\nReceive packet" + pkt);
         return pkt;
    }

    @Override
    public boolean sendPacket(IZKPacket packet) throws SerialPortException {
        log.debug("\nSend packet" + packet);
        return serialPort.writeBytes(packet.makePacket());
    }

    @Override
    public int getTemplate(List<Byte> template, Integer size) throws SerialPortException {
        int rows_log = 0;
        StringBuilder str_log = new StringBuilder("\n=========TEMPLATE DATA===========")
                .append("\nSize: ")
                .append(size)
                .append(" bytes\nTemplate: \n");
        for(byte b: serialPort.readBytes(size)){
            rows_log ++;
            template.add(b);
            str_log.append(String.format(" 0x%02x", b));
            if(rows_log%16 == 0){
                str_log.append("\n");
            }
        }
        log.debug(str_log);
        return template.size();
    }

    @Override
    public void destroy() throws Exception {
        if(serialPort.isOpened())
            serialPort.closePort();
    }
}
