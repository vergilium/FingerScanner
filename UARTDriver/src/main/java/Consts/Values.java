package Consts;

import Abstract.IFlag;

public enum Values implements IFlag {
    /** Not save the record. */         VAL_LOG_NOTSAVE((byte)0x30),
    /** Save the record. */             VAL_LOG_SAVE((byte)0x31),

    /** Timeout settings (never) */     VAL_NEVER_TIMEOUT((byte)0x30),

    /** Baudrate 115200 (default) */    VAL_BAUDRATE_115200((byte)0x35),
    /** Baudrate 57600 */               VAL_BAUDRATE_57600((byte)0x34),
    /** Baudrate 38400 */               VAL_BAUDRATE_38400((byte)0x33),
    /** Baudrate 19200 */               VAL_BAUDRATE_19200((byte)0x32),
    /** Baudrate 9600 */                VAL_BAUDRATE_9600((byte)0x31),

    /** Automatic response */           VAL_AUTO_ACK((byte)0x31),
    /** No response (default) */        VAL_NO_ACK((byte)0x30),

    /** Authorization mode */           VAL_AUTH_MODE((byte)0x30),
    /** Scanner mode */                 VAL_PHOTO_MODE((byte)0x31),
    /** Reader mode */                  VAL_TEMPLATE_MODE((byte)0x32),

    /** Led color green */              VAL_LED_GREEN((byte)0x80),
    /** Led color red */                VAL_LED_RED((byte)0x40),
    /** Led color yellow */             VAL_LED_YELLOW((byte)0xC0),

    /** Idle mode auto (default) */     VAL_IDLE_AUTO((byte)0x30),
    /** Idle mode manual */             VAL_IDLE_MANUAL((byte)0x31),

    /** Uniform effect ON */            VAL_UNIFORM_ON((byte)0x31),
    /** Uniform effect OFF (default) */ VAL_UNIFORM_OFF((byte)0x30),

    /** Template format ZKFinger v10 (default) */ VAL_FORMAT_ZK10((byte)0x30),
    /** Template format ANSI 378 */     VAL_FORMAT_ANSI((byte)0x31),
    /** Template format ISO 19794-2 */  VAL_FORMAT_ISO((byte)0x31);

    private final byte value;
    Values(byte value){
        this.value = value;
    }

    @Override
    public byte getValue() {
        return value;
    }

    public Values findLog(int n){
        Values[] logVal = {
                VAL_LOG_NOTSAVE,
                VAL_LOG_SAVE
        };
        return findValue(n, logVal);
    }

    public Values findTimeout(int n){
        if(n == VAL_NEVER_TIMEOUT.value){
            return VAL_NEVER_TIMEOUT;
        }
        return null;
    }

    public Values findBaudrate(int n){
        Values[] baudVal = {
                VAL_BAUDRATE_115200
                ,VAL_BAUDRATE_57600
                ,VAL_BAUDRATE_38400
                ,VAL_BAUDRATE_19200
                ,VAL_BAUDRATE_9600
        };
        return findValue(n, baudVal);
    }

    public Values findAutoresponse(int n){
        Values[] autoVal = {
                VAL_AUTO_ACK,
                VAL_NO_ACK
        };
        return findValue(n, autoVal);
    }

    public Values findMode(int n){
        Values[] modeVal = {
                VAL_AUTH_MODE
                ,VAL_PHOTO_MODE
                ,VAL_TEMPLATE_MODE
        };
        return findValue(n, modeVal);
    }

    public static Values findValue(byte n){
        return findValue(n, values());
    }

    private static Values findValue(int n, Values[] arr) {
        byte p = (byte)(n & 0x000000FF);
        for(Values v : arr)
            if(v.getValue() == p)
                return v;
        return null;
    }
}
