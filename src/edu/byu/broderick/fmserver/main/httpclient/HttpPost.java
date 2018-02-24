package edu.byu.broderick.fmserver.main.httpclient;


/**
 * Created by broderick on 3/22/17.
 */
public class HttpPost extends HttpRequest {

    public HttpPost(String path, String dataType, byte[] data) {
        this.path = path;
        this.dataType = dataType;
        this.data = data;
        this.requestType = HttpRequestType.POST;
    }
}
