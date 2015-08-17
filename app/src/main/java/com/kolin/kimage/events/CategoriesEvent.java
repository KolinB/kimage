package com.kolin.kimage.events;

import java.util.ArrayList;

/**
 * Created by kolin on 25/05/15.
 */
public class CategoriesEvent {

    private ArrayList<String> categories = new ArrayList<String>();

    public void addCategory(String cat) {
        categories.add(cat);
    }

    public ArrayList<String> getCategories() {
        return categories;
    }
}
