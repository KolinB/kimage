package com.kolin.kimage.network;

import com.kolin.kimage.events.ImageIDEvent;

/**
 * Created by kolin on 25/05/15.
 */
public interface INetworkSingleton {

    void getImageURLs(final ImageIDEvent imageIDs);

    void getImageIDs(String category, final int maxPics);

    void getCategories();

    void cancelPendingRequests();

}
