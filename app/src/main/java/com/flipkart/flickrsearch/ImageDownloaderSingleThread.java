package com.flipkart.flickrsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mayank.gupta on 19/12/15.
 */
//Another way is to subclass Thread class and  override run method. And in run method do
//    Looper.prepare;
//    Looper.loop - this will put a looper on the thread with message queue. Otherwise handler thread gives the looper itself
//    To use the message queue and send a message to the worker thread here, I need a Handler, Only using a handler can messages be delivered to the message queueu
//    A Handler object is associated to ONLY ONE THREAD. So here, we need two threads, one for UI thread, one for handler thread.
// Uses only one thread and a message queue to download images. So user may have more network bandwidth, but I am downloading one by one.
public class ImageDownloaderSingleThread extends HandlerThread {

    private static final String TAG = "ImageDownloaderThread";
    Handler downloaderThreadHandler;
    Handler uiThreadHandler;

    public ImageDownloaderSingleThread(Handler handler) {
        super(TAG);
        uiThreadHandler = handler;
    }

    public void downloadImage(ImageView iv, String url, int position) {
        Message message = Message.obtain();
        message.obj = iv;
        Bundle b = new Bundle();
        b.putInt("POSITION", position);
        b.putString("URL", url);
        message.setData(b);
        downloaderThreadHandler.sendMessage(message);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        Log.i(TAG, "Looper Prepared!!!!");

        downloaderThreadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle b = msg.getData();
                String url = b.getString("URL");
                final int position = b.getInt("POSITION");
                final ImageView iv = (ImageView) msg.obj;
                final Bitmap image = getImage(url);

//                The background thread cannot update the imageView object, thus we create a task message and send it to UI thread
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
//                        Code to stop flickering of images due to recycled view
                        int tagPosition = (int) iv.getTag();
                        if(tagPosition == position) {
                            iv.setImageBitmap(image);
                        }
                    }
                };
                uiThreadHandler.post(r);
            }
        };
    }

    private Bitmap getImage(String url) {
        try{
            URL link = new URL(url);
            URLConnection connection = link.openConnection();
            InputStream inputStream = connection.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(inputStream);
            return image;
        }catch(MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return  null;
    }
}
