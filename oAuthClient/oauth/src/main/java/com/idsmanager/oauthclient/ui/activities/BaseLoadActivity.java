package com.idsmanager.oauthclient.ui.activities;

import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.idsmanager.oauthclient.R;


/**
 * Created by YaLin on 2015/7/30.
 */
public abstract class BaseLoadActivity extends BaseDrawerActivity {

    ViewStub vsLoading;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base_loading);
        vsLoading = (ViewStub) findViewById(R.id.vs_loading);
        View view = View.inflate(this, layoutResID, null);
        FrameLayout content = (FrameLayout) findViewById(R.id.base_loading_fl_content);
        content.addView(view);
    }

    protected void showLoading() {
        if (vsLoading != null) {
            vsLoading.setVisibility(View.VISIBLE);
        }
    }

    protected void dismissLoading() {
        if (vsLoading != null) {
            vsLoading.setVisibility(View.GONE);
        }
    }
}
