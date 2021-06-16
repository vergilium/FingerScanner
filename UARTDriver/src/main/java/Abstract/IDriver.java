package Abstract;

import Consts.ErrFlag;
import Consts.Values;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDriver {

    int OpenDevice();
    int EnableDevice();

    int CloseDevice();

    int GetParameter(IFlag code, IZKPacket paramValue) throws InvalidParameterException;
    int SetParameter(IFlag code, Values param) throws InvalidParameterException;
    int ScanTemplate(List<Byte> template);

    /*
    int GetCapParams(long var0, int[] var2, int[] var3);
    int AcquireTemplate(long var0, byte[] var2, byte[] var3, int[] var4);
    int GenRegFPTemplate(byte[] var0, byte[] var1, byte[] var2, byte[] var3, int[] var4);
    long DBInit();
    int DBFree(long var0);
    int DBAdd(int var0, byte[] var1);
    int DBDel(int var0);
    int DBCount();
    int DBClear();
    int VerifyFPByID(int var0, byte[] var1);
    int MatchFP(byte[] var0, byte[] var1);
    int IdentifyFP(byte[] var0, int[] var1, int[] var2);
    int ExtractFromImage(String var0, int var1, byte[] var2, int[] var3);
    String BlobToBase64(byte[] var0, int var1);
    int Base64ToBlob(String var0, byte[] var1, int var2);
    int AcquireImage(long var0, byte[] var2);
    int DBSetParameter(long var0, int var2, int var3);
    int DBGetParameter(long var0, int var2, int[] var3);
    */

}
