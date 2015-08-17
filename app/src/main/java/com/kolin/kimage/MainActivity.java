package com.kolin.kimage;

//import android.app.Activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.kolin.kimage.events.CategoriesEvent;
import com.kolin.kimage.events.ImageIDEvent;
import com.kolin.kimage.events.ImageURLEvent;
import com.kolin.kimage.events.NetworkErrorEvent;
import com.kolin.kimage.network.INetworkSingleton;
import com.kolin.kimage.network.NetworkSingleton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import de.greenrobot.event.EventBus;

/**
 * Created by kolin on 25/05/15.
 */
public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private GridView gridView;
    private ProgressBar progressBar;
    private ImageIDEvent imageIDs;
    private CategoriesEvent categories;
    private INetworkSingleton net;
    private IImageRetriever imageRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);
        spinner = (Spinner) findViewById(R.id.spinner);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

       if (imageRetriever == null) imageRetriever = new ImageRetriever().getInstance();

        DialogFragment fragment = NetworkErrorDialog.newInstance("This uses the Shutterstock API to load image metadata and then images, and it deals with bad network conditions. When the image categories load, select the category.", getCategoriesRetryClick, null);
     //   fragment.show(getFragmentManager(), "intro");

    }

    private void setAdapter() {

        ImageAdapter adapter = new ImageAdapter(this, imageIDs.getImageIDs(), imageRetriever);
        gridView.setAdapter(adapter);
    }

    public void getCategories() {
        progressBar.setVisibility(View.VISIBLE);
        NetworkSingleton.getInstance().getCategories();
    }

    public void onEvent(CategoriesEvent cats) {
        progressBar.setVisibility(View.INVISIBLE);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cats.getCategories());
        this.categories = cats;
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onEvent(NetworkErrorEvent error) {
        progressBar.setVisibility(View.INVISIBLE);
        DialogFragment dialog = null;

        switch (error.getErrorType()) {
            case Constants.ERROR_TYPE_GETIDS:
                dialog = NetworkErrorDialog.newInstance(error.getError(), getIDsRetryClick, quitClick);
                break;
            case Constants.ERROR_TYPE_GETURLS:
                dialog = NetworkErrorDialog.newInstance(error.getError(), getURLsRetryClick, quitClick);
                break;
            case Constants.ERROR_TYPE_GETCATEGORIES:
                dialog = NetworkErrorDialog.newInstance(error.getError(), getCategoriesRetryClick, quitClick);
                break;
        }

        if (dialog != null) dialog.show(getFragmentManager(), "error");

    }

    private void getImageIDs(String categoryString) {

        progressBar.setVisibility(View.VISIBLE);
        NetworkSingleton.getInstance().getImageIDs(categoryString, Constants.MAX_IMAGES_PER_CATEGORY);

    }

    public void onEvent(ImageIDEvent IDs) {
        progressBar.setVisibility(View.INVISIBLE);
        this.imageIDs = IDs;
        getImageURLs();
    }

    private void getImageURLs() {
        progressBar.setVisibility(View.VISIBLE);
        NetworkSingleton.getInstance().getImageURLs(imageIDs);
    }

    public void onEvent(ImageURLEvent urls) {
        progressBar.setVisibility(View.INVISIBLE);
        setAdapter();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        NetworkSingleton.getInstance().cancelPendingRequests();
        getImageIDs(categories.getCategories().get(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private DialogInterface.OnClickListener getCategoriesRetryClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
        //    getCategories();
        }
    };

    private DialogInterface.OnClickListener getURLsRetryClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            getImageURLs();
        }
    };

    private DialogInterface.OnClickListener getIDsRetryClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            getImageIDs(categories.getCategories().get(spinner.getSelectedItemPosition()));
        }
    };

    private DialogInterface.OnClickListener quitClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        NetworkSingleton.getInstance().cancelPendingRequests();
    }
}
