package com.idsmanager.oauthclient.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 雅麟 on 2015/3/21.
 */
public class ToastUtil {
    private static Toast sToast;

    public static void showToast(Context context, String message) {
        if (context == null) {
            return;
        }

        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        sToast.show();
    }

    public static void showToast(Context context, int res) {
        if (context == null) {
            return;
        }

        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, context.getString(res), Toast.LENGTH_LONG);
        sToast.show();
    }
}
