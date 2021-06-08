package Consts;

import Abstract.IFlag;

public enum ErrFlag implements IFlag {
    /** The system is busy */       BUSY((byte)0x34),
    /** successful */               SUCCESS((byte)0x61),
    /** failed */                   FAIL((byte)0x63),
    /** time out */                 TIME_OUT((byte)0x64),
    /** parameter error */          PARAM_ERROR((byte)0x68),
    /** cannot be found or is not supported */ NOT_FOUND((byte)0x69),
    /** storage capacity is exceeded */ MEM_FULL((byte)0x6D),
    /** Maximum number of fingerprints allowed to
     be registered (10) */          FP_LIMIT((byte)0x72),
    /** Invalid ID number */        INVALID_ID((byte)0x76),
    /** executed command was canceled */ CANCELED((byte)0x81),
    /** Transfer data error */      DATA_ERROR((byte)0x82),
    /** already exists fingerprint */ EXIST_FINGER((byte)0x86),
    /** null flag for request */    NULL((byte)0x00),

    SID_SAVE_LOG((byte)0xA2);

    private final byte flag;

    ErrFlag(byte flag){
        this.flag = flag;
    }

    @Override
    public byte getValue(){
        return flag;
    }

    public static ErrFlag getFlag(byte n) {
        for(ErrFlag o : values())
            if(o.getValue() == n)
                return o;
        return null;
    }
}
