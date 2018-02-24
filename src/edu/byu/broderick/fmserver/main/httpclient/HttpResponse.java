package edu.byu.broderick.fmserver.main.httpclient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by broderick on 3/22/17.
 */
public class HttpResponse {

    private int status;
    private String url;
    private Map<String, List<String>> headers;
    private byte[] body;

    public HttpResponse(HttpURLConnection conn, byte[] bytes) {
        try {
            this.status = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.url = conn.getURL().toString();
        this.headers = conn.getHeaderFields();
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }
}
