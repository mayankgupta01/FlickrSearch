package com.flipkart.flickrsearch;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by mayank.gupta on 19/12/15.
 */
public class FlickrJSONTask extends AsyncTask<String, Void, ArrayList<FlickrItem>> {
    FlickrGetter flickrGetter;
    ArrayList<FlickrItem> flickrItems;
    ImageAdapter adapter;

    public FlickrJSONTask(ImageAdapter adapter,FlickrGetter flickrGetter, ArrayList<FlickrItem> flickrItems) {
        this.flickrGetter = flickrGetter;
        this.flickrItems = flickrItems;
        this.adapter = adapter;
    }

    @Override
    protected ArrayList<FlickrItem> doInBackground(String... params) {
        ArrayList<FlickrItem> fItems = flickrGetter.fetchItems(params[0]);
        return fItems;
    }

    @Override
    protected void onPostExecute(ArrayList<FlickrItem> flickrItems) {
        super.onPostExecute(flickrItems);
        if(flickrItems != null) {
            this.flickrItems.addAll(flickrItems);
        }

        adapter.notifyDataSetChanged();
    }
}
