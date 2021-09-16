package ua.ks.hogo.fingerscanner.uartdriver;

import lombok.extern.log4j.Log4j2;
import ua.ks.hogo.fingerscanner.uartdriver.Abstract.FingerDriver;
import ua.ks.hogo.fingerscanner.uartdriver.Abstract.Flag;
import ua.ks.hogo.fingerscanner.uartdriver.Abstract.IZKPacket;
import ua.ks.hogo.fingerscanner.uartdriver.Consts.Command;
import ua.ks.hogo.fingerscanner.uartdriver.Consts.ErrFlag;
import ua.ks.hogo.fingerscanner.uartdriver.Consts.SidFlag;
import ua.ks.hogo.fingerscanner.uartdriver.Consts.Values;

import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

import java.util.Arrays;
import java.util.List;

@Component
@Log4j2
public final class ZKTDriver implements FingerDriver {
    final Serial serial;

    public ZKTDriver(Serial serial) {
        this.serial = serial;
    }

    /**
     * Open device port and initialize port parameters
     * for communication with scanner
     * @return State after opened: -1 - Error; 0 - Not open; 1 - Opened
     */
    @Override
    public int OpenDevice() {
        try{
            serial.openPort();
            if(serial.isOpened()) {
                return 0;
            }
        } catch(Exception ex){
            log.error("Error opened device!", ex);
            return -1;
        }
        return 1;
    }

    public int EnableDevice() {
        try {
            if (serial.isOpened()) {
                ZKPacket enable = new ZKPacket(Command.MD_ENABLEDEVICE, null, null, null);
                serial.sendPacket(enable);
                serial.getPacket(enable);
                if(enable.flag == ErrFlag.SUCCESS){
                    log.info("Fingerprint device has been enabled!");
                    return 0;
                }
                log.warn("Fingerprinte device has NOT enabled!");
                return 1;
            }
            log.warn("Serial port can`t be opened");
            return 2;
        }catch(Exception ex){
            log.error("Fingerprint device can`t be enabled! Throwed undefined error!", ex);
            return -1;
        }
    }

    /**
     * Closing device port.
     * @return State after closing. 0 - Closed, -1 - Error, 1 - Not closed
     */
    @Override
    public int CloseDevice() {
        try{
            serial.closePort();
            if(!serial.isOpened()){
                log.info("Comunication with fingerprint device has been stoped!");
                return 0;
            }
        }catch(Exception ex){
            log.error("Error disabling the fingerprint device!", ex);
            return -1;
        }
        return 1;
    }

    /**
     * Get method of saved configuration in fingerprint module
     * @param code SID flag by needed parameter
     * @param paramValue {IZKPacket}
     * @return {int} Status value: 0 - success, -1 - error
     * @throws InvalidParameterException May be throw exception to invalid set parameter
     */
    @Override
    public int GetParameter(Flag code, IZKPacket paramValue) throws InvalidParameterException {
        Flag[] allowed_code = {
                SidFlag.SID_SAVE_LOG
                ,SidFlag.SID_AUTO_ACK
                ,SidFlag.SID_TIMEOUT
                ,SidFlag.SID_FW_VER
                ,SidFlag.SID_BAUDRATE
                ,SidFlag.SID_ENROLL_FP
                ,SidFlag.SID_FP_COUNT
                ,SidFlag.SID_USER_COUNT
                ,SidFlag.SID_USER_NUM
                ,SidFlag.SID_LOG_NUM
                ,SidFlag.SID_LOG_COUNT
                ,SidFlag.SID_BUILD_NUM
                ,SidFlag.SID_MODULE_IDENTIFY
                ,SidFlag.SID_TEMPLATE_FORMAT
        };

        if(!Arrays.asList(allowed_code).contains(code)){
            throw new InvalidParameterException("The value of `code` parameter is not allow");
        }

        try {
            ZKPacket packet = new ZKPacket(Command.MD_SYS_RP, null, null, code);
            serial.sendPacket(packet);
            serial.getPacket(paramValue);
            return 0;
        }catch(Exception ex){
            log.error("Error get parameter from fingerprint device");
            return -1;
        }
    }

    @Override
    public int SetParameter(Flag code, Values param) throws InvalidParameterException{
        Flag[] allowed_code = {
                SidFlag.SID_GPIO_LEVEL
                ,SidFlag.SID_SAVE_LOG
                ,SidFlag.SID_AUTO_ACK
                ,SidFlag.SID_TIMEOUT
                ,SidFlag.SID_MODULE_IDENTIFY
                ,SidFlag.SID_MODULE_ID
                ,SidFlag.SID_TEMPLATE_FORMAT
        };

        ZKPacket respose = new ZKPacket();

        if(!Arrays.asList(allowed_code).contains(code)){
            throw new InvalidParameterException("The value of `code` parameter is not allow");
        }

        try {
            ZKPacket packet = new ZKPacket(Command.MD_SYS_WP, (int) param.getValue(), null, code);
            serial.sendPacket(packet);
            serial.getPacket(respose);
            if(respose.flag == ErrFlag.SUCCESS){
                log.debug("Parameter " + code + " has been setted in value: " + param + ".");
                return 0;
            }
            log.debug("Can`t set parameter " + code + " with value: " + param + ".");
            return 1;
        }catch(Exception ex){
            log.error("Can`t set parameter. Throwed undefine error!", ex);
            return -1;
        }

    }

    public int ScanTemplate(List<Byte> template){
        try{
            ZKPacket packet = new ZKPacket(Command.MD_SCAN_TEMPLATE, null, null, null);
            serial.sendPacket(packet);
            serial.getPacket(packet);
            if(packet.size != 0){
                serial.getTemplate(template, packet.size);
            }
            return 0;
        }catch (Exception ex){
            log.error("Error geted new template!", ex);
            return -1;
        }
    }

    public int IdentifyFree(){
        try{
            ZKPacket packet = new ZKPacket(Command.MD_IDENTIFY_FREE, null, null, null);
            serial.sendPacket(packet);
            serial.getPacket(packet);
            if(packet.flag == ErrFlag.SUCCESS){
                log.info("====== SUCCESS: " + packet.param + " ==========");
                return packet.param;
            } else if(packet.flag == ErrFlag.FAIL){
                log.info("=====IDENTIFY FAILED========");
            }
            return 0;
        }catch (Exception ex){
            log.error("Error geted new template!", ex);
            return -1;
        }
    }


}