package ua.ks.hogo.fingerscanner.net;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.async.methods.*;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.ks.hogo.fingerscanner.config.RemoteConfig;
import ua.ks.hogo.fingerscanner.config.Settings;
import ua.ks.hogo.fingerscanner.utils.Sysinfo;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Http client class. Process to http request to api server.
 * @author Maloivan Oleksii
 * @version 1.0.0
 */
@Component
@Log4j2
public class HttpClient implements DisposableBean {

    /** Http async client object */
    private final CloseableHttpAsyncClient client;
    /** Http host object to remote server */
    private final HttpHost targetHost;

    /** Object of get hardvare information */
    @Autowired
    Sysinfo sysinfo;

    @Autowired
    RemoteConfig remoteConfig;

    /**
     * Constructor of http client.
     * @param config - Application global configuration
     * @throws ExecutionException - Exception from http client start()
     * @throws InterruptedException -
     */
    public HttpClient(Settings config) throws ExecutionException, InterruptedException {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(config.HTTP_REQUEST_TIMEOUT))
                .build();
        client = HttpAsyncClients.custom()
                .setIOReactorConfig(ioReactorConfig)
                .build();
        targetHost = new HttpHost(config.HTTP_SERVER_HOST, config.HTTP_SERVER_PORT);
        client.start();
    }

    /**
     * Http client close method. Called when application closed.
     */
    public void close() {
        log.debug("Http client shutting down.");
        client.close(CloseMode.GRACEFUL);
    }

    /**
     * Authorisation by remote server.
     * Request body has contained "login" and "password"
     * for authorisation. Login maked from const string and
     * join with last four symbols by MAC address. Password
     * has maked by full mac address.
     * @return Response from server. If responce status is OK(200)
     * body has contained JWT token.
     */
    public Future<SimpleHttpResponse> authorozation(){
        String mac = sysinfo.getMAC().toLowerCase();
        JsonObject requestBody = Json.createObjectBuilder()
                .add("login", "fingerscanner_" + mac.substring(12).replaceAll("[/.:-]",""))
                .add("password", mac)
                .build();
        return execHttpRequest(HttpEndpoint.DEVICE_SIGNIN, requestBody.toString().getBytes(UTF_8));
    }

    /**
     * Getting application configuration from server.
     * Request body has contained one required parameter - MAC address.
     * By MAC, remote server find storred configuration and
     * responced it in body as Json object.
     * @return Response from server. If responce status is OK(200)
     * body has contained Json object of configuration.
     */
    public Future<SimpleHttpResponse> getConfiguration() {
        JsonObject requestBody = Json.createObjectBuilder()
                .add("mac", sysinfo.getMAC())
                .build();

        return execHttpRequest(HttpEndpoint.DEVICE_INIT, requestBody.toString().getBytes(UTF_8), remoteConfig.getToken());
    }

    /**
     * Send finger template math request.
     * Request has contained required parameters: "filial" and "template"
     * in body for matching.
     * @param template - Byte array of finger template data from scanner.
     * @return Result matching response.
     */
    public Future<SimpleHttpResponse> matchTemplate(@NonNull List<Byte> template) {
        JsonArrayBuilder arr = Json.createArrayBuilder();
        for (Byte item : template) {
            arr.add(item);
        }

        JsonObject requestBody = Json.createObjectBuilder()
                .add("filial_id", remoteConfig.getFilial())
                .add("template", arr.build())
                .build();

        return execHttpRequest(HttpEndpoint.FINGER_MATCH, requestBody.toString().getBytes(UTF_8), remoteConfig.getToken());
    }

    /**
     * Private method for execute http request.
     * General method for all requerts.
     * @param endpoiunt - Url endpoint for request
     * @param body - body container
     * @param token - JWT token if assigned
     * @return Async responce.
     */
    private Future<SimpleHttpResponse> execHttpRequest(@NonNull HttpEndpoint endpoiunt, byte[] body, String token){
        final SimpleHttpRequest request = SimpleRequestBuilder.post()
                .setHeader("Content-Type", "application/json; utf-8")
                .addHeader("User-Agent", "FingerHttpAsyncClient")
                .addHeader("Authorization", "Bearer " + token)
                .setHttpHost(targetHost)
                .setPath(endpoiunt.getValue())
                .setBody(body, ContentType.APPLICATION_JSON)
                .build();

        return client.execute(
                SimpleRequestProducer.create(request),
                SimpleResponseConsumer.create(),
                new ResponseCallback(request));
    }

    /**
     * Prototype method for process http request without JWT token
     * @param endpoiunt - Url endpoint for request
     * @param body - body container
     * @return Async responce.
     */
    private Future<SimpleHttpResponse> execHttpRequest(@NonNull HttpEndpoint endpoiunt, byte[] body){
        return execHttpRequest(endpoiunt, body, null);
    }

    /**
     * Handler for all async http requests.
     */
    private static class ResponseCallback implements FutureCallback<SimpleHttpResponse> {
        private final SimpleHttpRequest request;

        public ResponseCallback(@NonNull SimpleHttpRequest request){
            this.request = request;
        }

        /**
         * Hendler for compleated requests.
         * @param response - Responce result.
         */
        @Override
        public void completed(SimpleHttpResponse response) {
            log.debug(request + "->" + new StatusLine(response));
            log.debug(response.getBody());
        }

        /**
         * Handler for errored requests.
         * @param ex throwed exception.
         */
        @Override
        public void failed(Exception ex) {
            log.error(request + "->" + ex);
        }

        /**
         * Handler for canceled requests
         */
        @Override
        public void cancelled() {
            log.debug(request + " cancelled");
        }
    }

    /**
     * Handler of close application.
     */
    @Override
    public void destroy() {
        this.close();
    }
}

