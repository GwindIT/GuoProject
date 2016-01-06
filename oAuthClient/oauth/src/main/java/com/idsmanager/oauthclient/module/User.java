package com.idsmanager.oauthclient.module;

/**
 * Created by 雅麟 on 2015/6/9.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * Created by 雅麟 on 2015/6/9.
 */
public class User {

    private static final String USER_SP = "login";

    public static final String USER_ACCOUNT_KEY = "user_account";
    public static final String LOGIN_TIMES_KEY = "login_times";

    public String username;
    public int loginTime;

    public static SharedPreferences getUserSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                USER_SP, Context.MODE_PRIVATE);
        return sp;
    }

    public static boolean isLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                USER_SP, Context.MODE_PRIVATE);
        String accessToken = sp.getString(USER_ACCOUNT_KEY, null);
        return !TextUtils.isEmpty(accessToken);
    }

    public static String getUserAccount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                USER_SP, Context.MODE_PRIVATE);
        return sp.getString(USER_ACCOUNT_KEY, null);
    }

    public static int getLoginTimes(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                USER_SP, Context.MODE_PRIVATE);
        return sp.getInt(LOGIN_TIMES_KEY, 0);
    }

    /**
     * when login success, should store user info
     *
     * @return
     */
    public static void storeUserInfo(Context context, User user) {
        if (user == null) {
            throw new IllegalArgumentException("user can not be null");
        }
        SharedPreferences sp = context.getSharedPreferences(
                USER_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_ACCOUNT_KEY, user.username);
        editor.putInt(LOGIN_TIMES_KEY, user.loginTime);
        editor.apply();
    }

    /**
     * when logout success, should delete user info
     *
     * @return
     */
    public static void deleteUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                USER_SP, Context.MODE_PRIVATE);
        sp.edit()
                .putString(USER_ACCOUNT_KEY, null)
                .apply();
    }
}
