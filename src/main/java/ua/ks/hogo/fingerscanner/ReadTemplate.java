package ua.ks.hogo.fingerscanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import ua.ks.hogo.fingerscanner.config.RemoteConfig;
import ua.ks.hogo.fingerscanner.net.HttpClient;
import ua.ks.hogo.fingerscanner.sound.*;
import ua.ks.hogo.fingerscanner.uartdriver.Abstract.FingerDriver;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Log4j2
public class ReadTemplate extends TimerTask {
    private final FingerDriver driver;
    private final HttpClient httpClient;
    private final RemoteConfig remoteConfig;
    private int connectionAttempt = 10;
    private final Player player;

    public ReadTemplate(FingerDriver driver, HttpClient httpClient, RemoteConfig remoteConfig, Player player) {
        this.driver = driver;
        this.httpClient = httpClient;
        this.remoteConfig = remoteConfig;
        this.player = player;
        player.init();
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
            try {
                SimpleHttpResponse signIn = httpClient.authorozation().get();
                if (signIn.getCode() != 200) return;
                String token = signIn.getBodyText();
                if (token == null || token.equals("")) return;
                remoteConfig.setToken(token);
            }catch (Exception ex){
                log.error(ex.getMessage());
                log.debug(ex);
                return;
            }
        }

        if(remoteConfig.getFilial() == null){
            try {
                connectionAttempt--;
                SimpleHttpResponse resp = httpClient.getConfiguration().get();
                ObjectMapper mapper = new ObjectMapper();
                if (resp.getCode() != 200) return;
                Map<String, Object> map = mapper.readValue(resp.getBodyText(), new TypeReference<Map<String,Object>>(){});
                if(!map.get("status").equals(0)) return;
                remoteConfig.Init(mapper.convertValue(map.get("response"), RemoteConfig.class));
                player.enableAudio();
                player.play(SoundCommand.INIT_SUCCESS);
            }catch(Exception ex){
                log.error(ex.getMessage());
                log.debug(ex);
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
                ObjectMapper mapper = new ObjectMapper();
               SimpleHttpResponse resp = httpClient.matchTemplate(template).get();
               if(resp.getCode() != 200) {
                   return;
               }
                Map<String, Object> map = mapper.readValue(resp.getBodyText(), new TypeReference<Map<String,Object>>(){});
                if(!map.get("status").equals(0)) return;
                Fingerprint fp = mapper.convertValue(map.get("response"), Fingerprint.class);
                if(fp.matchResult){
                    player.play(SoundCommand.AUTH_SUCCESS);
                } else {
                    player.play(SoundCommand.AUTH_FAIL);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
