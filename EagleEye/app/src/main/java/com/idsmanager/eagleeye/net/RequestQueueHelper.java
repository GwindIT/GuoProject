package com.idsmanager.eagleeye.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;


/**
 * Created by 雅麟 on 2015/3/27.
 */
public class RequestQueueHelper {
    private static RequestQueue sInstance;

    public static RequestQueue getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Volley.newRequestQueue(context, new OkHttpStack(new OkHttpClient()));
        }
        return sInstance;
    }

    public static RequestQueue getInstance() {
        return sInstance;
    }

    public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
        return Volley.newRequestQueue(context, stack);
    }
}
