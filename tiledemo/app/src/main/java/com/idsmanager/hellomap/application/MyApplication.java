package com.idsmanager.hellomap.application;

import android.app.Application;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by wind on 2016/1/11.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
