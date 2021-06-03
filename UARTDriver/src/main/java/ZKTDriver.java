import Abstract.IDriver;
import Abstract.ISerial;


public final class ZKTDriver implements IDriver {

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

    @Override
    public int CloseDevice(long var0) {
        return 0;
    }

    @Override
    public int GetParameter(long var0, int var2, byte[] var3, int[] var4) {
        return 0;
    }

    @Override
    public int SetParameter(long var0, int var2, byte[] var3, int var4) {
        return 0;
    }
}