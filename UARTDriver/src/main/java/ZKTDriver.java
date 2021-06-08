import Abstract.IDriver;
import Abstract.IFlag;
import Abstract.ISerial;
import Abstract.IZKPacket;
import Consts.Commands;
import Consts.ErrFlag;
import Consts.SidFlag;
import Consts.Values;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;


public final class ZKTDriver implements IDriver {
    private IZKPacket _packet = null;

    /**
     * Open device port and initialize port parameters
     * for communication with scanner
     * @return State after opened: -1 - Error; 0 - Not open; 1 - Opened
     */
    @Override
    public int OpenDevice() {
        try{
            ISerial serial = Serial.initPort();
            serial.openPort();
            if(serial.isOpened()) return 1;
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            return -1;
        }
        return 0;
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
     * @param paramValue null
     * @param size null
     * @return {int} Status value: 0 - success, -1 - error
     * @throws InvalidParameterException May be throw exception of invalid set parameter
     */
    @Override
    public int GetParameter(IFlag code, IZKPacket paramValue) throws InvalidParameterException {
        IFlag[] allowed_code = {
                SidFlag.SID_SAVE_LOG,
                SidFlag.SID_AUTO_ACK,
                SidFlag.SID_TIMEOUT,
                SidFlag.SID_FW_VER,
                SidFlag.SID_BAUDRATE,
                SidFlag.SID_ENROLL_FP,
                SidFlag.SID_FP_COUNT,
                SidFlag.SID_USER_COUNT,
                SidFlag.SID_USER_NUM,
                SidFlag.SID_LOG_NUM,
                SidFlag.SID_LOG_COUNT,
                SidFlag.SID_BUILD_NUM,
                SidFlag.SID_MODULE_IDENTIFY
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
    public int SetParameter(long var0, int var2, byte[] var3, int var4) {
        return 0;
    }
}