package ua.ks.hogo.fingerscanner.net;

public enum HttpEndpoint {
    DEVICE_SIGNIN("/login"),
    DEVICE_INIT("/api/finger/scanner/init"),
    FINGER_MATCH("/api/finger/template/matchZk");

    private final String endpoint;
    HttpEndpoint(String endpoint){
        this.endpoint = endpoint;
    }

    public String getValue() {
        return endpoint;
    }
}
