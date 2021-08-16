package ua.ks.hogo.fingerscanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import ua.ks.hogo.fingerscanner.config.RemoteConfig;
import ua.ks.hogo.fingerscanner.net.HttpClient;
import ua.ks.hogo.fingerscanner.uartdriver.Abstract.FingerDriver;
import org.springframework.stereotype.Component;

//import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class ReadTemplate extends TimerTask {
    private final FingerDriver driver;
    private final HttpClient httpClient;
    private final RemoteConfig remoteConfig;
    private int connectionAttempt = 10;

    public ReadTemplate(FingerDriver driver, HttpClient httpClient, RemoteConfig remoteConfig) {
        this.driver = driver;
        this.httpClient = httpClient;
        this.remoteConfig = remoteConfig;
    }

    @SneakyThrows
    @Override
    public void run() {

        if(connectionAttempt == 0) {
            log.error("Coud not get configuration. Exit application!");
            Runtime.getRuntime().exit(0);
            return;
        }

        if(remoteConfig.getToken() == null || Objects.equals(remoteConfig.getToken(), "")){
            SimpleHttpResponse signIn = httpClient.authorozation().get();
            if(signIn.getCode() != 200) return;
            String token = signIn.getBodyText();
            if(token == null || token.equals("")) return;
            remoteConfig.setToken(token);
        }

        if(remoteConfig.getFilial() == null){
            try {
                connectionAttempt--;
                SimpleHttpResponse resp = httpClient.getConfiguration().get();
                ObjectMapper mapper = new ObjectMapper();
                if (resp.getCode() != 200) return;
                remoteConfig.Init(mapper.readValue(resp.getBodyText(), RemoteConfig.class));
            }catch(ExecutionException ex){
                log.warn(ex.getMessage());
                return;
            }
        }

        if(BooleanUtils.isFalse(remoteConfig.getScannerEnable()) || BooleanUtils.isTrue(remoteConfig.getIsBlocked())) {
            return;
        }

        List<Byte> template = new ArrayList<>(2048);

        driver.ScanTemplate(template);

        if(template.size() > 0){
            //URL url = null;
            try {
               SimpleHttpResponse resp = httpClient.matchTemplate(template).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
