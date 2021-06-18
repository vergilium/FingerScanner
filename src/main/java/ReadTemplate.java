import Abstract.IDriver;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class ReadTemplate extends TimerTask {
    private final IDriver driver = new ZKTDriver();

    @Override
    public void run() {
        List<Byte> template = new ArrayList<>(2048);
        driver.ScanTemplate(template);

        //driver.IdentifyFree();
        if(template.size() > 0){
            byte[] b1 = new byte[template.size()];

            for (int i = 0; i < template.size(); i++)
            {
                b1[i] = template.get(i);
            }
            FingerprintTemplate _template = new FingerprintTemplate(b1);
            double score = new FingerprintMatcher(_template)
                    .match(_template);
            if(score > 40){
                System.out.println("======================================\n===============SUCCESS================\n======================================\n");
            }

        }
    }
}
