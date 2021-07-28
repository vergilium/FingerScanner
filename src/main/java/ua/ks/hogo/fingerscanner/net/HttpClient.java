package ua.ks.hogo.fingerscanner.net;

import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.async.methods.*;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.function.Supplier;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.nio.AsyncPushConsumer;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.nio.HandlerFactory;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.reactor.IOReactorStatus;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import ua.ks.hogo.fingerscanner.config.Settings;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Log4j2
public class HttpClient implements DisposableBean {

    private final CloseableHttpAsyncClient client;
    private final HttpHost targetHost;
    private final String[] enpointsUris;

    public HttpClient(Settings config) throws ExecutionException, InterruptedException {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(config.HTTP_REQUEST_TIMEOUT))
                .build();
        client = HttpAsyncClients.custom()
                .setIOReactorConfig(ioReactorConfig)
                .build();
        targetHost = new HttpHost(config.HTTP_SERVER_HOST, config.HTTP_SERVER_PORT);
        enpointsUris = new String[] {"/", "/ip", "/user-agent", "/headers"};
        client.start();
    }

    public void close() {
        log.debug("Http client shutting down.");
        client.close(CloseMode.GRACEFUL);
    }


    public Future<SimpleHttpResponse> matchTemplate(List<Byte> template) {
        String str = "{\"filial_id\": 1, \"template\":" +
                        Arrays.toString(template.toArray()) +
                        "}";

        final SimpleHttpRequest request = SimpleRequestBuilder.post()
                .setHeader("Content-Type", "application/json; utf-8")
                .setHttpHost(targetHost)
                .setPath("/finger/match")
                .setBody(str.getBytes(UTF_8), ContentType.APPLICATION_JSON)
                .build();

        log.debug("Executing request " + request);

        return client.execute(
                SimpleRequestProducer.create(request),
                SimpleResponseConsumer.create(),
                new ResponseCallback(request)
        );
        //future.get();
    }

    public void getConfiguration() throws SocketException {
        final NetworkInterface netInf = NetworkInterface.getNetworkInterfaces().nextElement();
        final byte[] mac = netInf.getHardwareAddress();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        log.info("Mac addr: {}", sb.toString());
    }

    public static class ResponseCallback implements FutureCallback<SimpleHttpResponse> {
        private final SimpleHttpRequest request;

        public ResponseCallback(SimpleHttpRequest request){
            this.request = request;
        }

        @Override
        public void completed(SimpleHttpResponse response) {
            log.debug(request + "->" + new StatusLine(response));
            log.debug(response.getBody());
        }

        @Override
        public void failed(Exception ex) {
            log.debug(request + "->" + ex);
        }

        @Override
        public void cancelled() {
            log.debug(request + " cancelled");
        }
    }

    @Override
    public void destroy() throws Exception {
        client.close();
    }
}

