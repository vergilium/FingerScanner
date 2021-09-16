package ua.ks.hogo.fingerscanner.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "uart")
@Getter
@Setter(value = AccessLevel.MODULE)
public class UARTConfig {
    private String port;
    private Integer baudrate;
    private short stopbit;
    private short parity;
    private short databits;
    private int recTimeout;
}
