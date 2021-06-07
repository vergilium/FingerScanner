package Abstract;

public interface IZKPacket {
    byte PACKET_START = 0x70;
    byte PACKET_STOP = 0x0A;
    byte[] makePacket();
    boolean getReceived();
    boolean getErrored();
}
