package ua.ks.hogo.fingerscanner;

import ua.ks.hogo.fingerscanner.uartdriver.Abstract.FingerDriver;
import ua.ks.hogo.fingerscanner.uartdriver.Abstract.IZKPacket;
import ua.ks.hogo.fingerscanner.uartdriver.Consts.SidFlag;
import ua.ks.hogo.fingerscanner.uartdriver.Consts.Values;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.ks.hogo.fingerscanner.uartdriver.ZKPacket;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class FingerScanner implements CommandLineRunner {
    private final FingerDriver driver;
    private final TimerTask readTemplate;

    public FingerScanner(FingerDriver driver, TimerTask readTemplate) {
        this.driver = driver;
        this.readTemplate = readTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(FingerScanner.class, args);
    }

    @Override
    public void run(String... args){
        try {
            Timer sTimer = new Timer();
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
