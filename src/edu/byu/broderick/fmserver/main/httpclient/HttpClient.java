package edu.byu.broderick.fmserver.main.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by broderick on 3/22/17.
 */
public class HttpClient {
    protected int connectionTimeout = 2000;
    protected int readTimeout = 4000;
    protected boolean isConnected;
    protected String basePath = "";
    protected HttpRequestHandler requestHandler;
    private Map<String, String> headers;

    public HttpClient(String basePath) {
        this.requestHandler = new HttpRequestHandler();
        this.basePath = basePath;
        headers = new TreeMap<>();
    }

    public HttpURLConnection openConnection(String path) throws IOException {
        String fullPath = basePath + path;
        try {
            new URL(fullPath);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL path");
        }
        return requestHandler.openConnection(fullPath);
    }

    public void readyConnection(HttpURLConnection urlConnection, HttpRequestType type, String contentType) throws IOException {
        urlConnection.setConnectTimeout(connectionTimeout);
        urlConnection.setReadTimeout(readTimeout);
        requestHandler.prepareConnection(urlConnection, type, contentType);
    }

    public int writeOutput(HttpURLConnection urlConnection, byte[] data) throws IOException {
        OutputStream out = requestHandler.openOutputStream(urlConnection);
        out.write(data);
        return urlConnection.getResponseCode();
    }

    public HttpResponse doPost(String path, String dataType, byte[] data) {
        return doRequest(new HttpPost(path, dataType, data));
    }

    public HttpResponse doGet(String path, String dataType, byte[] data) {
        return doRequest(new HttpGet(path, dataType, data));
    }

    public HttpResponse doRequest(HttpRequest request) {

        String path = request.getPath();
        HttpRequestType type = request.getRequestType();
        String dataType = request.getDataType();
        byte[] data = request.getData();

        OutputStream out = null;
        InputStream in = null;

        HttpURLConnection conn = null;
        HttpResponse response = null;

        try {
            isConnected = false;
            conn = openConnection(path);
            readyConnection(conn, type, dataType);
            appendHeaders(conn);

            conn.connect();
            isConnected = true;

            if (conn.getDoOutput() && data != null) {
                out = requestHandler.openOutputStream(conn);
                out.write(data);
            }

            if (conn.getDoInput()) {
                in = requestHandler.openInputStream(conn);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] inData = new byte[65536];
                int n;
                while ((n = in.read(inData)) != -1) {
                    buffer.write(inData, 0, n);
                }
                buffer.flush();
                response = new HttpResponse(conn, buffer.toByteArray());
            } else {
                response = new HttpResponse(conn, null);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (conn != null)
                conn.disconnect();
        }

        return response;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void clearHeaders() {
        this.headers.clear();
    }

    public void appendHeaders(HttpURLConnection urlConnection) {
        for (String name : headers.keySet()) {
            String value = headers.get(name);
            urlConnection.setRequestProperty(name, value);
        }
    }

}
