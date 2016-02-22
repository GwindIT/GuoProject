package com.idsmanager.eagleeye.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by wind on 2016/1/11.
 */
public class EagleApplication extends Application{
    private static Context mContext;
    private static Handler mHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        mContext = getApplicationContext();
        mHandler = new Handler();
    }
    public static Context getContext(){
        return mContext;
    }

    public static Handler getMainHandler(){
        return  mHandler;
    }
}
