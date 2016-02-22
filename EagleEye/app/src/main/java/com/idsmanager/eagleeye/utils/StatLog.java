package com.idsmanager.eagleeye.utils;

import android.util.Log;

/**
 * Created by wind on 2016/1/21.
 */
public class StatLog {
    public static final String LOG_TAG = "EAGLE EYE";
    public static boolean DEBUG = true;


    public static void printLog(String tag, String msg) {
        if (!DEBUG) {
            return;
        }
        String procInfo = "Process id: " + android.os.Process.myPid()
                + " Thread id: " + Thread.currentThread().getId() + " ";
        Log.d(tag, procInfo + msg);
    }

    public static final void analytics(String log) {
        if (DEBUG)
            Log.d(LOG_TAG, log);
    }

    public static final void error(String log) {
        if (DEBUG)
            Log.e(LOG_TAG, "" + log);
    }

    public static final void logInfo(String log) {
        if (DEBUG)
            Log.i(LOG_TAG, log);
    }

    public static final void logInfo(String tag, String log) {
        if (DEBUG)
            Log.i(tag, log);
    }

    public static final void logv(String log) {
        if (DEBUG)
            Log.v(LOG_TAG, log);
    }

    public static final void warn(String log) {
        if (DEBUG)
            Log.w(LOG_TAG, log);
    }
}
