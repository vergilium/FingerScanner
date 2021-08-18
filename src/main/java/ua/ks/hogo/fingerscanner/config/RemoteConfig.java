package ua.ks.hogo.fingerscanner.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteConfig {
    @JsonProperty
    private Integer filial;
    @JsonProperty
    private Boolean bellEnable;
    @JsonProperty
    private Boolean audioEnable;
    @JsonProperty
    private Integer audioLevel;
    @JsonProperty
    private Boolean scannerEnable;
    @JsonProperty
    private Boolean isBlocked;
    @JsonProperty
    private String serverLocale;
    @JsonProperty
    private String ntpServer;
    @JsonProperty
    private String latestFirmware;
    @JsonProperty
    private String firmwareCrc;

    private String token;

    public void Init(@NonNull RemoteConfig parent){
        this.filial = parent.getFilial();
        this.bellEnable = parent.getBellEnable();
        this.audioEnable = parent.getAudioEnable();
        this.audioLevel = parent.getAudioLevel();
        this.scannerEnable = parent.getScannerEnable();
        this.isBlocked = parent.getIsBlocked();
        this.serverLocale = parent.getServerLocale();
        this.ntpServer = parent.getNtpServer();
        this.latestFirmware = parent.getLatestFirmware();
        this.firmwareCrc = parent.getFirmwareCrc();
    }

    public void setToken(String token){
        this.token = token;
    }
}
