package Consts;

public enum Commands {
    /*
     * System configuration
     * */
    /** Write system parameters */  MD_SYS_WP((byte)0x11),
    /** Save system parameters */   MD_SYS_SP((byte)0x12),
    /** Read system parameters */   MD_SYS_RP((byte)0x13),
    /** Query System status */      MD_SYS_STATUS((byte)0x14),

    /*
     * Enrollment
     */
    /** Register the user fingerprint template by scanning three times */
    MD_ENROLL_SCAN((byte)0x15),
    /** Fingerprint template is registered via fingerprint image */
    MD_ENROLL_IMAGE_X((byte)0x80),
    /** Register the user through the fingerprint template */
    MD_ENROLL_TMP((byte)0x17),

    /*
     * Verification
     */
    /** 1: 1 Verification if that the user template exists */
    MD_VERIFY_SCAN((byte)0x18),

    /*
     * Identification
     */
    /** Identify users through fingerprint images */
    MD_IDENTIFY_IMAGE_X((byte)0x81),
    /** Scan recognition in FreeScan mode */
    MD_IDENTIFY_FREE((byte)0x2F),

    /*
     * Delete
     */
    /** Delete all fingerprint templates */
    MD_DEL_ALL_TMP((byte)0x27),
    /** Deletes the fingerprint template for the specified user */
    MD_DEL_TMP((byte)0x26),

    /*
     * Fingerprint template
     */
    /** Reads the specified user's fingerprint template */
    MD_READ_TMP_X((byte)0x89),
    /** Scans the fingerprint template of the current finger */
    MD_SCAN_TEMPLATE((byte)0xFC),

    /*
     * User
     */
    /** Add a user */       MD_ADD_USER((byte)0xF1),
    /** Read a user */      MD_READ_USER((byte)0xF2),
    /** Delete the user */  MD_DELETE_USER((byte)0xF3),
    /** Delete all users */ MD_DEL_ALL_USER((byte)0xF5),

    /**
     * Fingerprint image
     */
    MD_SCAN_IMAGE_X((byte)0x83),

    /*
     * Time and records
     */
    /** set the current time */     MD_SET_TIME((byte)0x4A),
    /** Get module time */          MD_GET_TIME((byte)0x4B),
    /** Delete all records */       MD_DEL_ALOG((byte)0x9E),
    /** Read all the log data */    MD_LOAD_LOG_X((byte)0x9E),

    /*
     * Database
     */
    /** Read all user data */
    MD_LOAD_USER_X((byte)0xA0),
    /** Read all fingerprint template data */
    MD_LOAD_TMP_X((byte)0xA2),
    /** Delete all data */
    MD_DEL_DB((byte)0xF8),
    /** Upload the fingerprint template storage file */
    MD_WT_FILE_X((byte)0xAA),
    /** Upload the user information storage file */
    MD_WU_FILE_X((byte)0x42),
    /** Download the fingerprint template storage file */
    MD_RT_FILE_X((byte)0xAB),
    /** Download the user information storage file */
    MD_RU_FILE_X((byte)0x43),

    /**
     * Upgrade Firmware Version
     */
    MD_UPDATE_FW((byte)0x72),

    /**
     * Module reset
     * Performs a soft reset of the module
     */
    MD_RESET((byte)0xD0),

    /**
     * Enable the module
     */
    MD_ENABLEDEVICE((byte)0xFB),

    /**
     * Disable module
     */
    MD_DISABLEDEVICE((byte)0xFA);


    private final byte command;

    Commands(byte command) {
        this.command = command;
    }

    public byte getValue() {
        return command;
    }

    public static Commands getCommand(byte n){
        for(Commands c : values())
            if(c.getValue() == n)
                return c;
        return null;
    }
}
