package ua.ks.hogo.fingerscanner.uartdriver.Abstract;

import ua.ks.hogo.fingerscanner.uartdriver.Consts.Values;

import java.security.InvalidParameterException;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface FingerDriver {
    int OpenDevice();
    int EnableDevice();
    int CloseDevice();
    int GetParameter(Flag code, IZKPacket paramValue) throws InvalidParameterException;
    int SetParameter(Flag code, Values param) throws InvalidParameterException;
    int ScanTemplate(List<Byte> template);
    int IdentifyFree();
}
