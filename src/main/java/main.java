import Abstract.IDriver;
import Consts.ErrFlag;
import Consts.SidFlag;

public class main {
    public static void main(String[] args){

        try {
            IDriver driver = new ZKTDriver();
            if(driver.OpenDevice() == 1){
                System.out.println("Port opened!");
                driver.GetParameter(SidFlag.SID_BAUDRATE, null, null);
            }
            while (true) {
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
