package com.idsmanager.basicclient.utils;

import android.content.Context;

import com.jin.uitoolkit.snackbar.Snackbar;
import com.jin.uitoolkit.snackbar.SnackbarManager;
import com.jin.uitoolkit.snackbar.listeners.ActionClickListener;
import com.idsmanager.basicclient.R;

/**
 * Created by YaLin on 2015/12/15.
 */
public class SnackBarUtil {

    public static void showSnack(Context context, String message) {
        if (context == null) {
            return;
        }
        SnackbarManager.show(
                Snackbar.with(context)
                        .text(message)
                        .textColorResource(R.color.snackbar_text_color)
                        .colorResource(R.color.snackbar_background_color)
                        .actionLabel(context.getString(R.string.confirm))
                        .actionColorResource(R.color.snackbar_action_color)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                            }
                        })
                        .duration(Snackbar.SnackbarDuration.LENGTH_LONG));
    }

    public static void showSnack(Context context, int res) {
        if (context == null || res <= 0) {
            return;
        }

        SnackbarManager.show(
                Snackbar.with(context)
                        .text(res)
                        .textColorResource(R.color.snackbar_text_color)
                        .colorResource(R.color.snackbar_background_color)
                        .actionLabel(context.getString(R.string.confirm))
                        .actionColorResource(R.color.snackbar_action_color)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                            }
                        })
                        .duration(Snackbar.SnackbarDuration.LENGTH_LONG));
    }

}
