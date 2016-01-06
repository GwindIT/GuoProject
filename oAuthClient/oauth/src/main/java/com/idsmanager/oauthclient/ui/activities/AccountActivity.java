package com.idsmanager.oauthclient.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jin.uitoolkit.activity.RevealBackgroundActivity;
import com.jin.uitoolkit.util.PixelUtil;
import com.idsmanager.oauthclient.R;
import com.idsmanager.oauthclient.ui.fragments.LoginFragment;
import com.idsmanager.oauthclient.ui.fragments.TokenLoginFragment;
/**
 * Created by 雅麟 on 2015/6/9.
 */
public class AccountActivity extends RevealBackgroundActivity implements LoginFragment.LoginSuccessCallback {
    public enum OpenType {
        Login, TokenLogin
    }

    private static OpenType current;

    private static String OPEN_TYPE_KEY = "open_type";

    public static void open(Activity activity, OpenType type, int[] location) {
        Intent intent = new Intent(activity, AccountActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, location);
        intent.putExtra(OPEN_TYPE_KEY, type);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }


    public static void requestLogin(Activity activity) {
        if (current == OpenType.Login) {
            return;
        }
        current = OpenType.Login;
        int[] location = new int[2];
        location[0] = PixelUtil.getScreenWidth(activity) / 2;
        location[1] = PixelUtil.getScreenHeight(activity) / 2;
        requestLogin(activity, location);
    }

    public static void requestLogin(Activity activity, int[] location) {
        open(activity, OpenType.Login, location);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        OpenType openType = (OpenType) getIntent().getSerializableExtra(OPEN_TYPE_KEY);
        switchContent(openType);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        current = null;
        overridePendingTransition(0, R.anim.push_right_out);
    }

    private void switchContent(OpenType type) {
        switch (type) {
            case Login:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rl_content, LoginFragment.getInstance(this))
                        .commit();
                break;
        }
    }

    public void addContent(OpenType type) {
        switch (type) {
            case TokenLogin:
                TokenLoginFragment.open(R.id.rl_content, getSupportFragmentManager(), null);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onLoginSuccess() {
        finish();
    }

}
