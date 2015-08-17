package com.kolin.kimage.events;

/**
 * Created by kolin on 25/05/15.
 */
public class ImageURLEvent {
    private String url = null;

    public ImageURLEvent() {
    }

    public ImageURLEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}

