package edu.byu.broderick.fmserver.main.httpclient;

/**
 * Created by broderick on 3/22/17.
 */
public enum HttpRequestType {

    GET(true, false),
    POST(true, true),
    PUT(true, true),
    DELETE(true, false),
    HEAD(false, false);

    private boolean doInput;
    private boolean doOutput;

    HttpRequestType(boolean doInput, boolean doOutput) {
        this.doInput = doInput;
        this.doOutput = doOutput;
    }

    public String getTypeName() {
        return this.toString();
    }

    public boolean isDoInput() {
        return doInput;
    }

    public boolean isDoOutput() {
        return doOutput;
    }
}
