package com.idsmanager.basicclient.application;

import android.app.Application;
import android.content.Context;

import com.idsmanager.basicclient.utils.StatLog;

import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * Created by YaLin on 2015/12/11.
 */
public class MyApplication extends Application {
    private static Context mContext;
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
 //   private  int loginNum;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable ex) {
                StringWriter wr = new StringWriter();
                PrintWriter err = new PrintWriter(wr);
                ex.printStackTrace(err);

                StatLog.printLog("app exception", wr.toString() + defaultUncaughtExceptionHandler.getClass().getName());

                defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        });
    }

    public static Context getContext() {
        return mContext;
    }


//    public void setLoginNum(int loginNum){
//        this.loginNum = loginNum;
//    }
//    public  int getLoginNum(){
//        return  loginNum;
//    }
}
