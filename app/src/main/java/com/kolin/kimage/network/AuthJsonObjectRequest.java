package com.kolin.kimage.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Map;

/**
 * Created by kolin on 25/05/15.
 */
public class AuthJsonObjectRequest<T> extends JsonObjectRequest {
    Map<String, String> headers;
 
    public AuthJsonObjectRequest(String str, Response.Listener listener, Response.ErrorListener errorlistener, Map<String, String> headers) {
        super(str, listener, errorlistener);
        this.headers = headers;
        this.setRetryPolicy(new DefaultRetryPolicy(2000, 5, 1.5f));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

}
