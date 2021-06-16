import Abstract.IDriver;
import Abstract.IZKPacket;
import Consts.SidFlag;
import Consts.Values;
import db.DB;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public class main {
    public static void main(String[] args){
        try {
            DB db = DB.getInstance();
            if(db.sync() != 0){

            }


            IDriver driver = new ZKTDriver();
            Timer sTimer = new Timer();
            TimerTask readTemplate = new ReadTemplate();

            if(driver.OpenDevice() == 0){
                System.out.println("Port opened!");
                if(driver.EnableDevice() == 0){
                    IZKPacket packet = new ZKPacket();
                    driver.SetParameter(SidFlag.SID_MODULE_IDENTIFY, Values.VAL_TEMPLATE_MODE);
                    driver.GetParameter(SidFlag.SID_MODULE_IDENTIFY, packet);
                    sTimer.schedule(readTemplate, 0, 1000);
                }
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
