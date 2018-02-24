package edu.byu.broderick.fmserver.main.httpclient;

/**
 * Created by broderick on 3/22/17.
 */
public class HttpRequest {

    protected String path;
    protected HttpRequestType requestType;
    protected String dataType;
    protected byte[] data;

    public String getPath() {
        return path;
    }

    public HttpRequestType getRequestType() {
        return requestType;
    }

    public String getDataType() {
        return dataType;
    }

    public byte[] getData() {
        return data;
    }
}
