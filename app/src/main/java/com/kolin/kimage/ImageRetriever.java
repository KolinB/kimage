package com.kolin.kimage;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by kolin on 25/05/15.
 */
public class ImageRetriever implements IImageRetriever {

    private static ImageRetriever mInstance;
    private DisplayImageOptions options;

    public ImageRetriever() {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

    }

    public static synchronized ImageRetriever getInstance() {
        if (mInstance == null) {
            mInstance = new ImageRetriever();
        }
        return mInstance;
    }

    public void displayImage(String url, ImageView view) {
        ImageLoader.getInstance()
                .displayImage(url, view, options, new SimpleImageLoadingListener(), null);

    }



}
