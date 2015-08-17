package com.kolin.kimage.network;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kolin.kimage.Constants;
import com.kolin.kimage.URLCache;
import com.kolin.kimage.events.CategoriesEvent;
import com.kolin.kimage.events.ImageIDEvent;
import com.kolin.kimage.events.ImageURLEvent;
import com.kolin.kimage.events.NetworkErrorEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by kolin on 25/05/15.
 */
public class NetworkSingleton implements INetworkSingleton {

    private static NetworkSingleton mInstance;
    VolleyController mController;

    private final Map<String, String> headers = new HashMap();
    private String encodedCredentials = "NDk1YjZhMWE4YTg3ZmQwNDgwZDE6ZGU3ZDdjYjkxNDU0Y2YyMmE3ZDQ0NjQ5Y2FjYmY3YjY1ZDE4MmQyMg==";

    private NetworkSingleton(VolleyController controller) {

        mController = controller;
        headers.put("Authorization", "Basic " + encodedCredentials);

    }


    public static void initialize (VolleyController controller) {
        mInstance = new NetworkSingleton(controller);
    }

    public static synchronized NetworkSingleton getInstance() {
        return mInstance;
    }


    public void getCategories() {
        AuthJsonObjectRequest jsonObjectRequest = new AuthJsonObjectRequest
                (Constants.CATEGORIES_URL,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("", "AAA 1 " + response);

                                JSONArray array = null;
                                try {
                                    array = response.getJSONArray("data");
                                    CategoriesEvent cat = new CategoriesEvent();
                                    for (int i = 0; i < array.length(); i++) {
                                        cat.addCategory(array.getJSONObject(i).getString("name"));
                                        Log.d("", "AAA adding cat " + array.getJSONObject(i).getString("name"));
                                    }

                                    EventBus.getDefault().post(cat);

                                } catch (JSONException e) {
                                    Log.d("", "AAA 2 JSONE ");

                                    EventBus.getDefault().post(new NetworkErrorEvent(Constants.ERROR_TYPE_GETCATEGORIES, "Problem reading category JSON"));
                                }

                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("", "AAA 3 error");
                                EventBus.getDefault().post(new NetworkErrorEvent(Constants.ERROR_TYPE_GETCATEGORIES, "Network error getting categories"));
                            }
                        },
                        headers
                );

        mController.addToRequestQueue(jsonObjectRequest);
    }


    public void getImageIDs(String category, final int maxPics) {
        String url = Constants.SEARCH_URL + "?category=" + URLEncoder.encode(category) + "&per_page=" + maxPics;

        AuthJsonObjectRequest jsonObjectRequest = new AuthJsonObjectRequest
                (url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                JSONArray array = null;
                                try {
                                    ImageIDEvent IDs = new ImageIDEvent();
                                    array = response.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        IDs.addID(array.getJSONObject(i).getString("id"));
                                    }
                                    EventBus.getDefault().post(IDs);

                                } catch (JSONException e) {
                                    EventBus.getDefault().post(new NetworkErrorEvent("Problem reading IDs JSON"));
                                }

                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                EventBus.getDefault().post(new NetworkErrorEvent(Constants.ERROR_TYPE_GETIDS, "Network error getting imageIDs"));
                            }
                        },
                        headers
                );

        mController.addToRequestQueue(jsonObjectRequest);
    }

    private int totalURLs;
    private int numErrors;

    public void getImageURLs(final ImageIDEvent imageIDs) {

        numErrors = 0;

        totalURLs = imageIDs.getImageIDs().size();

        for (int i = 0; i < imageIDs.getImageIDs().size(); i++) {

            final String ID = imageIDs.getImageIDs().get(i);
            String url = Constants.GETURL_URL + ID;

            if (URLCache.getInstance().isInCache(ID)) {
                totalURLs--;
                continue;
            }

            AuthJsonObjectRequest req = new AuthJsonObjectRequest
                    (url,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String url = response.getJSONObject("assets").getJSONObject("preview").getString("url");
                                        URLCache.getInstance().addToCache(ID, url);
                                        if (--totalURLs <= 0)
                                            imageURLFinished();

                                    } catch (JSONException e) {
                                        numErrors++;
                                        if (--totalURLs <= 0)
                                            imageURLFinished();
                                    }

                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    numErrors++;
                                    if (--totalURLs <= 0)
                                        imageURLFinished();
                                }
                            },
                            headers
                    );
            mController.addToRequestQueue(req);
        }

        if (totalURLs == 0) imageURLFinished();
    }

    private void imageURLFinished() {
        if (numErrors > 0)
            EventBus.getDefault().post(new NetworkErrorEvent(Constants.ERROR_TYPE_GETURLS, "Error getting image URLs"));
        else EventBus.getDefault().post(new ImageURLEvent());
    }

    @Override
    public void cancelPendingRequests() {
        mController.cancelPendingRequests();
    }

}





