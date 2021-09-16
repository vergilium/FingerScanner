package ua.ks.hogo.fingerscanner.uartdriver.Consts;

import ua.ks.hogo.fingerscanner.uartdriver.Abstract.Flag;

public enum SidFlag implements Flag {
    /**
     * Save the record.
     * Whether to save the record when performing fingerprint verification or recognition.
     * @see Values#VAL_LOG_SAVE
     * @see Values#VAL_LOG_NOTSAVE
     */
    SID_SAVE_LOG((byte)0x36),

    /**
     * Automatic response.
     * @see Values#VAL_AUTO_ACK
     * @see Values#VAL_NO_ACK
     * */
    SID_AUTO_ACK((byte)0x82),

    /**
     * Timeout settings.
     * Values:
     * 0x31 - 1 sec
     * ...
     * 0x3A - 10 sec (default)
     * ...
     * 0x44 - 20 sec
     * ...
     * @see Values#VAL_NEVER_TIMEOUT
     */
    SID_TIMEOUT((byte)0x62),

    /**
     * Firmware version.
     */
    SID_FW_VER((byte)0x6E),

    /**
     * Baudrate settings
     * @see Values#VAL_BAUDRATE_115200
     * @see Values#VAL_BAUDRATE_57600
     * @see Values#VAL_BAUDRATE_38400
     * @see Values#VAL_BAUDRATE_19200
     * @see Values#VAL_BAUDRATE_9600
     */
    SID_BAUDRATE((byte)0x71),

    /**
     * Enrolled fingerprints.
     * The number of currently enrolled fingerprints.
     */
    SID_ENROLL_FP((byte)0x73),

    /**
     * Max fingerprints.
     * Maximum number of fingerprints that can be enrolled
     */
    SID_FP_COUNT((byte)0x74),

    /**
     * Max. User.
     * Allowed maximum number of users enrollment.
     */
    SID_USER_COUNT((byte)0x79),

    /**
     * Enrolled users.
     * The number of currently registered users
     */
    SID_USER_NUM((byte)0x7A),

    /**
     * Records.
     * The number of currently saved verification records.
     */
    SID_LOG_NUM((byte)0x7C),

    /**
     * Max. Records.
     * Maximum numbers of verification records that can be saved.
     */
    SID_LOG_COUNT((byte)0x7B),

    /**
     * Firmware date.
     */
    SID_BUILD_NUM((byte)0x89),

    /**
     * Fingerprint Module ID.
     * Values: 0 ~ 65535 (1 - default)
     */
    SID_MODULE_ID((byte)0x6D),

    /**
     * LED indicator control.
     * Values:
     * to upper 8bit indicate the LED color
     * @see Values#VAL_LED_GREEN
     * @see Values#VAL_LED_RED
     * @see Values#VAL_LED_YELLOW
     * The lower 8bit indicate the display time of the LED,
     * in the range of 1 to 255 sec.
     */
    SID_GPIO_LEVEL((byte)0x31),

    /**
     * Working Module.
     * Authorization mode: identification inside the module. Default.
     * @see Values#VAL_AUTH_MODE
     * Scanner(picture): keep sending the captured image, and send to host device.
     * @see Values#VAL_PHOTO_MODE
     * Reader(template): send the extracted fingerprint templates to host device for verification.
     * @see Values#VAL_TEMPLATE_MODE
     */
    SID_MODULE_IDENTIFY((byte)0x50),

    /**
     * Template format.
     * Set the fingerprint templates format you want.
     * Note: Module can only support ZKTeco fingerprint template format verification inside locally.
     */
    SID_TEMPLATE_FORMAT((byte)0x51),

    /**
     * Algorithm version
     */
    SID_ALGORITHM((byte)0xAA),

    /**
     * Reset parameters.
     * Reset all parameters to default values
     */
    SID_RESET((byte)0xA2),

    /**
     * Idle time.
     * Set the time period of the idle mode.
     * @see Values#VAL_IDLE_AUTO
     * @see Values#VAL_IDLE_MANUAL
     */
    SID_IDLE_TIME((byte)0xA3),

    /**
     * Sleep mode.
     * Set the module to sleep.
     */
    SID_SLEEP((byte)0xA5),

    /**
     * Uniform effect.
     * Get one image Uniform effect.
     * @see Values#VAL_UNIFORM_ON
     * @see Values#VAL_UNIFORM_OFF
     */
    SID_UNIFORM((byte)0xA6);


    private final byte flag;

    SidFlag(byte flag){
        this.flag = flag;
    }

    @Override
    public byte getValue() {
        return flag;
    }

}
