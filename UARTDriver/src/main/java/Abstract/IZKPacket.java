package Abstract;

import Consts.Values;

public interface IZKPacket {
    short PACKET_SIZE = 13;
    byte PACKET_START = 0x70;
    byte PACKET_STOP = 0x0A;
    byte[] makePacket();
    boolean getReceived();
    boolean getErrored();

    IZKPacket receivePacket(byte[] data );
    IZKPacket receivePacket(byte data );

    Integer getParam();
}
