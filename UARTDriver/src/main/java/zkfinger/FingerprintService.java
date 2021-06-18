package zkfinger;

import java.io.File;


public class FingerprintService {
    public FingerprintService() {
    }

    public static native int MatchFP(byte[] var0, byte[] var1);

    static {
        try {
            System.loadLibrary("zkfp");
        }catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }
}
