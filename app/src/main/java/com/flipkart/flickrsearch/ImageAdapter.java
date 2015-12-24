package com.flipkart.flickrsearch;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by mayank.gupta on 19/12/15.
 */
public class ImageAdapter extends BaseAdapter {

    ArrayList<FlickrItem> flickrItems;
    Context context;
    ImageDownloaderThread imageDownloaderThread;

    public ImageAdapter(ArrayList<FlickrItem> flickrItems, Context context) {
        this.flickrItems = flickrItems;
        this.context = context;
        imageDownloaderThread = new ImageDownloaderThread(new Handler());
        imageDownloaderThread.start();
    }

    @Override
    public int getCount() {
        return flickrItems.size();
    }

    @Override
    public Object getItem(int position) {
        return flickrItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mainView = null;

        if(convertView == null) {
            mainView = LayoutInflater.from(context).inflate(R.layout.item, null);
        } else {
            mainView = convertView;
        }

        ImageView imageView = (ImageView) mainView.findViewById(R.id.imageView);
//        To stop flickering of images.
        imageView.setTag(position);
        FlickrItem item = flickrItems.get(position);

//        download the image from item.url and display - Need to implement this in a background thread
        imageDownloaderThread.downloadImage(imageView, item.url, position);

        return mainView;
    }
}
