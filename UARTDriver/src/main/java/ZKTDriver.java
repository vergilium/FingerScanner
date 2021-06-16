import Abstract.IDriver;
import Abstract.IFlag;
import Abstract.ISerial;
import Abstract.IZKPacket;
import Consts.Commands;
import Consts.ErrFlag;
import Consts.SidFlag;
import Consts.Values;
import jssc.SerialPortException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class ZKTDriver implements IDriver {

    /**
     * Open device port and initialize port parameters
     * for communication with scanner
     * @return State after opened: -1 - Error; 0 - Not open; 1 - Opened
     */
    @Override
    public int OpenDevice() {
        try{
            Serial serial = (Serial) Serial.initPort();
            serial.openPort();
            if(serial.isOpened()) {
                return 0;
            }
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            return -1;
        }
        return 1;
    }

    public int EnableDevice() {
        try {
            Serial serial = (Serial) Serial.initPort();
            if (serial.isOpened()) {
                ZKPacket enable = new ZKPacket(Commands.MD_ENABLEDEVICE, null, null, null);
                serial.sendPacket(enable);
                serial.getPacket(enable);
                if(enable.flag == ErrFlag.SUCCESS){
                    System.out.println("Fingerprinter device has enabled!");
                    return 0;
                }
                System.out.println("Fingerprinter device has NOT enabled!");
                return 1;
            }
            System.out.println("Port has not opened");
            return 2;
        }catch(Exception ex){
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
            ISerial serial = Serial.initPort();
            serial.closePort();
            if(!serial.isOpened()) return 0;
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return -1;
        }
        return 1;
    }

    /**
     * Get method of saved configuration in fingerprint module
     * @param code SID flag by needed parameter
     * @param paramValue {IZKPacket}
     * @return {int} Status value: 0 - success, -1 - error
     * @throws InvalidParameterException May be throw exception of invalid set parameter
     */
    @Override
    public int GetParameter(IFlag code, IZKPacket paramValue) throws InvalidParameterException {
        IFlag[] allowed_code = {
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
        };

        if(!Arrays.asList(allowed_code).contains(code)){
            throw new InvalidParameterException("The value of `code` parameter is not allow");
        }

        try {
            ZKPacket packet = new ZKPacket(Commands.MD_SYS_RP, null, null, code);
            Serial serial = (Serial) Serial.initPort();
            serial.sendPacket(packet);
            serial.getPacket((ZKPacket) paramValue);
            return 0;
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return -1;
        }
    }

    @Override
    public int SetParameter(IFlag code, Values param) throws InvalidParameterException{
        IFlag[] allowed_code = {
                SidFlag.SID_GPIO_LEVEL
                ,SidFlag.SID_SAVE_LOG
                ,SidFlag.SID_AUTO_ACK
                ,SidFlag.SID_TIMEOUT
                ,SidFlag.SID_MODULE_IDENTIFY
                ,SidFlag.SID_MODULE_ID
        };

        ZKPacket respose = new ZKPacket();

        if(!Arrays.asList(allowed_code).contains(code)){
            throw new InvalidParameterException("The value of `code` parameter is not allow");
        }

        try {
            ZKPacket packet = new ZKPacket(Commands.MD_SYS_WP, (int) param.getValue(), null, code);
            Serial serial = (Serial) Serial.initPort();
            serial.sendPacket(packet);
            serial.getPacket(respose);
            if(respose.flag == ErrFlag.SUCCESS){
                return 0;
            }
            return 1;
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            return -1;
        }

    }

    public int ScanTemplate(List<Byte> template){
        try{
            ZKPacket packet = new ZKPacket(Commands.MD_SCAN_TEMPLATE, null, null, null);
            Serial serial = (Serial) Serial.initPort();
            serial.sendPacket(packet);
            serial.getPacket(packet);
            if(packet.size != 0){
                serial.getTemplate(template, packet.size);
            }
            return 0;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return -1;
        }
    }
}