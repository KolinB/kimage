package com.kolin.kimage;

import android.app.Application;

import com.kolin.kimage.network.NetworkSingleton;
import com.kolin.kimage.network.VolleyController;

/**
 * Created by kolin on 28/05/15.
 */
public class KimageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkSingleton.initialize(new VolleyController(getApplicationContext()));
    }
}
