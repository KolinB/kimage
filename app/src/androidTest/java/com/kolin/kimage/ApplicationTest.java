package com.kolin.kimage;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.kolin.kimage.network.NetworkSingleton;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        NetworkSingleton.initialize(new VolleyControllerTest(getContext()));
    }
}