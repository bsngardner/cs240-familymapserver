package edu.byu.broderick.fmserver.main.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by broderick on 3/22/17.
 */
public class HttpRequestHandler {

    public HttpURLConnection openConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        return uc;
    }

    public void prepareConnection(HttpURLConnection urlConnection, HttpRequestType requestType,
                                  String contentType) throws IOException {

        urlConnection.setRequestMethod(requestType.getTypeName());
        urlConnection.setDoOutput(requestType.isDoOutput());
        urlConnection.setDoInput(requestType.isDoInput());

        if (contentType != null) {
            urlConnection.setRequestProperty("Content-Type", contentType);
        }

        urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
    }

    public OutputStream openOutputStream(HttpURLConnection urlConnection) throws IOException {
        return urlConnection.getOutputStream();
    }

    public InputStream openInputStream(HttpURLConnection urlConnection) throws IOException {
        return urlConnection.getInputStream();
    }
}
