import Abstract.IDriver;
import Abstract.IZKPacket;
import Consts.ErrFlag;
import Consts.SidFlag;

import java.util.concurrent.CompletableFuture;

public class main {
    public static void main(String[] args){

        try {
            IDriver driver = new ZKTDriver();
            if(driver.OpenDevice() == 1){
                System.out.println("Port opened!");
                IZKPacket packet = new ZKPacket();
                driver.GetParameter(SidFlag.SID_BAUDRATE, packet);
                System.out.println(packet.getParam());
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
