import Abstract.IDriver;
import Abstract.IZKPacket;
import Consts.SidFlag;
import Consts.Values;

import java.util.Timer;
import java.util.TimerTask;

public class FingerScanner {
    public static void main(String[] args){
        try {

            IDriver driver = new ZKTDriver();
            Timer sTimer = new Timer();
            TimerTask readTemplate = new ReadTemplate();

            if(driver.OpenDevice() == 0){
                if(driver.EnableDevice() == 0){
                    IZKPacket packet = new ZKPacket();
                    driver.SetParameter(SidFlag.SID_MODULE_IDENTIFY, Values.VAL_TEMPLATE_MODE);
                    driver.GetParameter(SidFlag.SID_MODULE_IDENTIFY, packet);
                    driver.SetParameter(SidFlag.SID_TEMPLATE_FORMAT, Values.VAL_FORMAT_ZK10);
                    driver.GetParameter(SidFlag.SID_TEMPLATE_FORMAT, packet);
                    sTimer.schedule(readTemplate, 0, 1000);
                }
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
