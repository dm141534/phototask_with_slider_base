package com.daimajia.slider.demo.adater;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.demo.util.LruBitmapCache;

/**
 * Created by christianjandl on 07.09.15.
 */
public class MyVolleySingleton {

    private static MyVolleySingleton mVolleyInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    private MyVolleySingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
                new LruBitmapCache(LruBitmapCache.getCacheSize(context))
        );
    }

    public static synchronized MyVolleySingleton getInstance(Context context) {
        if (mVolleyInstance == null) {
            mVolleyInstance = new MyVolleySingleton(context);
        }
        return mVolleyInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // use the application context
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <t> void addToRequestQueue(Request<t> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
