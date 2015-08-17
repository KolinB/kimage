package com.kolin.kimage.events;

/**
 * Created by kolin on 25/05/15.
 */
public class NetworkErrorEvent {

    private final String errorString;
    private int errorType = 0;

    public NetworkErrorEvent(String str) {
        errorString = str;
    }

    public NetworkErrorEvent(int type, String str) {
        errorType = type;
        errorString = str;
    }

    public int getErrorType() {
        return errorType;
    }

    public String getError() {
        return errorString;
    }
}
