import Abstract.IFlag;
import Abstract.IZKPacket;
import Consts.Commands;
import Consts.ErrFlag;

public class ZKPacket implements IZKPacket {
    private final byte[] packet;

    public ZKPacket(Commands cmd, Integer param, Integer size, IFlag flag){
        packet = new byte[13];
        packet[0] = PACKET_START;
        packet[1] = cmd.getCommand();
        if(param != null){
            System.arraycopy(IntToBytes(param),0, packet, 2, 4);
        }
        if(size != null) {
            System.arraycopy(IntToBytes(size), 0, packet, 6, 4);
        }
        packet[10] = flag.getFlag();
        packet[12] = PACKET_STOP;
    }

    private static byte[] IntToBytes(int number){
        byte[] buff = new byte[4];
        buff[3] = (byte)(number >> 24);
        buff[2] = (byte)((number >> 16) & 0x00FF);
        buff[1] = (byte)((number >> 8) & 0x0000FF);
        buff[0] = (byte)(number & 0x000000FF);
        return buff;
    }

    private static byte getCRC(byte[] buffer){
        byte crc = 0;
        for(int i=0; i<11; i++){
            crc += buffer[i];
        }
        return (byte)(crc&0xFF);
    }

    @Override
    public byte[] getPacket() {
        packet[11] = getCRC(packet);
        return packet;
    }
}
