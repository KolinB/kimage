package com.kolin.kimage.events;

import java.util.ArrayList;

/**
 * Created by kolin on 25/05/15.
 */
public class ImageIDEvent {
    private ArrayList<String> IDs = new ArrayList<String>();

    public void addID(String ID) {
        IDs.add(ID);
    }

    public ArrayList<String> getImageIDs() {
        return IDs;
    }

}
