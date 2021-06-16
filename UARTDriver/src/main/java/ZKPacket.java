import Abstract.IFlag;
import Abstract.IZKPacket;
import Consts.Commands;
import Consts.ErrFlag;
import Consts.SidFlag;
import Consts.Values;

import java.security.InvalidParameterException;
import java.util.Arrays;

public class ZKPacket implements IZKPacket {
    private final byte[] packet;
    private int index;
    private boolean received = false;
    private boolean errored = false;

    public Commands command;
    public Integer param;
    public Integer size;
    public IFlag flag;

    public ZKPacket(Commands cmd, Integer param, Integer size, IFlag flag){
        packet = new byte[13];
        this.command = cmd;
        this.param = param;
        this.size = size;
        this.flag = flag;
    }

    public ZKPacket(){
        packet = new byte[13];
        command = null;
        this.param = null;
        this.size = null;
        this.flag = null;
    }

    @Override
    public boolean getReceived(){
        return received;
    }

    @Override
    public boolean getErrored(){
        return errored;
    }

    @Override
    public Integer getParam(){
       return param;
    }

    @Override
    public byte[] makePacket() throws InvalidParameterException {
        packet[0] = PACKET_START;
        if(command != null) { packet[1] = command.getValue(); }
        else { throw new InvalidParameterException("The `command` parameter is null"); }
        if(param != null) { System.arraycopy(IntToBytes(param),0, packet, 2, 4); }
        if(size != null) { System.arraycopy(IntToBytes(size), 0, packet, 6, 4); }
        packet[10] = flag != null ? flag.getValue() : 0;
        packet[11] = getCRC(packet);
        packet[12] = PACKET_STOP;
        return packet;
    }

    public IZKPacket receivePacket(byte data ){
        if(!this.received) {
            if (data == PACKET_STOP && index == 12) {
                this.packet[12] = PACKET_STOP;
                this.received = true;
                this.errored = false;
                index = 0;
                this.command = Commands.getCommand(packet[1]);
                this.param = BytesToInt(Arrays.copyOfRange(packet, 2,6));
                this.size = BytesToInt(Arrays.copyOfRange(packet, 6,10));
                this.flag = ErrFlag.getFlag(packet[10]);
                return this;
            }
            packet[index] = data;
            index++;
        }
        return null;
    }

    @Override
    public IZKPacket receivePacket(byte[] data ){
        if (data[12] == PACKET_STOP && data[11] == getCRC(data)) {
            command = Commands.getCommand(data[1]);
            param = BytesToInt(Arrays.copyOfRange(data, 2,6));
            size = BytesToInt(Arrays.copyOfRange(data, 6,10));
            flag = ErrFlag.getFlag(data[10]);
            return this;
        }
        errored = true;
        return null;
    }

    private static byte[] IntToBytes(int number){
        byte[] buff = new byte[4];
        buff[3] = (byte)(number >> 24);
        buff[2] = (byte)((number >> 16) & 0x00FF);
        buff[1] = (byte)((number >> 8) & 0x0000FF);
        buff[0] = (byte)(number & 0x000000FF);
        return buff;
    }

    private static int BytesToInt(byte[] arr){
        int buff = 0;
        buff = Byte.toUnsignedInt(arr[0]);
        buff |= (arr[1] << 8);
        buff |= (arr[2] << 16);
        buff |= (arr[3] << 24);
        return buff;
    }

    private static byte getCRC(byte[] buffer){
        byte crc = 0;
        for(int i=0; i<11; i++){
            crc += buffer[i];
        }
        return (byte)(crc&0xFF);
    }
}
