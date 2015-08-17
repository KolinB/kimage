package com.kolin.kimage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by kolin on 25/05/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> imageIDs;
    private IImageRetriever imageRetriever;

    public ImageAdapter(Context c, ArrayList<String> imageIDs, IImageRetriever imageRetriever) {
        mContext = c;
        this.imageIDs = imageIDs;
        this.imageRetriever = imageRetriever;

    }

    public int getCount() {
        return imageIDs.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(R.drawable.blank);
        final String imageID = imageIDs.get(position);
        final String cacheURL = URLCache.getInstance().get(imageID);

        if (cacheURL != null) {
            imageRetriever.displayImage(cacheURL, imageView);

        }

        return imageView;
    }

}