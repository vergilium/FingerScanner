package ua.ks.hogo.fingerscanner.net;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Map;

@Getter
@ConfigurationProperties(prefix = "endpoint")
public class HttpEndpoint {
    private final Map<String, String> httpEndpoint;

    public HttpEndpoint(Map<String, String> httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
    }
}
