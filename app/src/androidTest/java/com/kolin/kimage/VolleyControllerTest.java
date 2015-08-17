package com.kolin.kimage;

import android.content.Context;

import com.android.volley.Request;
import com.kolin.kimage.network.VolleyController;

/**
 * Created by kolin on 28/05/15.
 */
public class VolleyControllerTest extends VolleyController {

    public VolleyControllerTest(Context context) {
        super(context);
    }

    @Override
    public <T> void addToRequestQueue(Request<T> req) {

    }
}
