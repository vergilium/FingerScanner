package ua.ks.hogo.fingerscanner.sound;

public enum SoundCommand {
    AUTH_SUCCESS,
    AUTH_FAIL,
    INIT_SUCCESS,
    INIT_FAIL;

    public static SoundCommand fromString(String val) {
        for (SoundCommand c : SoundCommand.values()) {
            if (c.name().equalsIgnoreCase(val)) {
                return c;
            }
        }
        return null;
    }
}
