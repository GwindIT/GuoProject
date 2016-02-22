package com.idsmanager.eagleeye.utils;

import android.widget.Toast;

import com.idsmanager.eagleeye.application.EagleApplication;

/**
 * Created by wind on 2016/1/21.
 */
public class ToastUtil {
    private static Toast toast;

    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(EagleApplication.getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }
}
