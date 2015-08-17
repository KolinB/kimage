package com.kolin.kimage;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.kolin.kimage.events.CategoriesEvent;
import com.kolin.kimage.events.ImageIDEvent;
import com.kolin.kimage.events.NetworkErrorEvent;
import com.kolin.kimage.network.NetworkSingleton;
import com.robotium.solo.Solo;

import java.util.concurrent.CountDownLatch;

import de.greenrobot.event.EventBus;

/**
 * Created by kolin on 15/08/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2 {

    private CountDownLatch latch=null;
    private Solo solo;
    private String category;


    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        EventBus.getDefault().register(this);
        latch = new CountDownLatch(1);
        solo=new Solo(getInstrumentation(),getActivity());



    }

    public void tearDown() throws Exception {
        EventBus.getDefault().unregister(this);
    }

    public void testGetCategories() throws Exception {

        final Activity activity = getActivity();
        assert (activity != null);
//        assertFalse(activity!=null);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) activity).getCategories();
            }
        });

        latch.await();
        latch=new CountDownLatch(1);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NetworkSingleton.getInstance().getImageIDs(category, Constants.MAX_IMAGES_PER_CATEGORY);

            }
        });

        latch.await();

    }

    public void onEvent(ImageIDEvent IDs) {

        assert(IDs!=null);
        assert(IDs.getImageIDs().size()==0);
        latch.countDown();
    }

    public void onEvent(CategoriesEvent cats) {

        Log.d("","KKK ONEVENT1 "+cats+" "+cats.getCategories().size());
        assert (cats != null);
        assert(cats.getCategories().size()>0);
    //    assertFalse(cats != null);
      category=cats.getCategories().get(0);
        latch.countDown();

    }

    public void onEvent(NetworkErrorEvent error) {
        Log.d("","KKK ONEVENT2");
        assert(error!=null);
        assertFalse(error!=null);
        latch.countDown();
    }

}