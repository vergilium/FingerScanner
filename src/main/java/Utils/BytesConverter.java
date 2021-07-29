package Utils;

import java.nio.ByteBuffer;

public final class BytesConverter {
    public static byte[] IntToBytes(int number){
        return  ByteBuffer.allocate(4).putInt(number).array();
    }

    public static int BytesToInt(byte[] arr){
        return ByteBuffer.wrap(arr).getInt();

    }
}
