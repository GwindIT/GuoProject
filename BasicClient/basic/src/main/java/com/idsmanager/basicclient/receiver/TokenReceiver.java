package com.idsmanager.basicclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.idsmanager.ssosublibrary.constants.Constants;
import com.idsmanager.basicclient.module.User;
import com.idsmanager.basicclient.ui.activities.MainActivity;
import com.idsmanager.basicclient.utils.StatLog;

/**
 * Created by YaLin on 2015/11/24.
 */

public class TokenReceiver extends BroadcastReceiver {
    private static final String TAG = TokenReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        StatLog.printLog(TAG, "received action: " + action);
        if (Constants.ACTION_RP_TOKEN_RECEIVED.equals(action)) {
            String token = intent.getStringExtra(Constants.TOKEN_KEY);
            MainActivity.open(context, token);
        } else if (Constants.ACTION_RP_SLO.equals(action)) {
            User.deleteUserInfo(context);
            StatLog.printLog(TAG, "slo in " + context.getPackageName());
        }
    }
}
