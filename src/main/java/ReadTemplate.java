import Abstract.IDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class ReadTemplate extends TimerTask {
    private final IDriver driver = new ZKTDriver();

    @Override
    public void run() {
        List<Byte> template = new ArrayList<>(2048);
        driver.ScanTemplate(template);
        if(template.size() > 0){
            System.out.println(template);
        }
    }
}
