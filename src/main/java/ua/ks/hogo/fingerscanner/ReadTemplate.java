package ua.ks.hogo.fingerscanner;

import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import ua.ks.hogo.fingerscanner.net.HttpClient;
import ua.ks.hogo.fingerscanner.uartdriver.Abstract.FingerDriver;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

@Component
@Log4j2
public class ReadTemplate extends TimerTask {
    private final FingerDriver driver;
    private final HttpClient httpClient;

    public ReadTemplate(FingerDriver driver, HttpClient httpClient) {
        this.driver = driver;
        this.httpClient = httpClient;
    }

    @Override
    public void run() {
        List<Byte> template = new ArrayList<>(2048);
        driver.ScanTemplate(template);

        if(template.size() > 0){
            URL url = null;
            try {

                SimpleHttpResponse resp = httpClient.matchTemplate(template).get();

                httpClient.getConfiguration();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
