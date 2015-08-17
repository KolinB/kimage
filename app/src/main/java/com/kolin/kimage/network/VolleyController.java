package com.kolin.kimage.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by kolin on 28/05/15.
 */
public class VolleyController {

    private RequestQueue requestQueue;

    public VolleyController(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag("Global");
        requestQueue.add(req);
    }

    public void cancelPendingRequests() {
        requestQueue.cancelAll("Global");
    }
}
