package com.idsmanager.basicclient.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.idsmanager.basicclient.R;
import com.idsmanager.basicclient.ui.fragments.SettingFragment;


/**
 * Created by YaLin on 2015/7/30.
 */
public class SettingActivity extends BaseLoadActivity implements SettingFragment.SettingItemClickCallback {

    enum ContentType {
        PersonalInfo
    }

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        switchContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected int getDrawerCheckId() {
        return R.id.navigation_setting;
    }

    private void switchContent() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_content, SettingFragment.getInstance(this))
                .commit();

    }

    public void addContent(ContentType contentType) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.push_left_in,
                R.anim.push_left_out,
                R.anim.push_right_in,
                R.anim.push_right_out);
        switch (contentType) {
            case PersonalInfo:
                break;
        }
    }


    @Override
    public void onPersonalInfoClicked() {
        addContent(ContentType.PersonalInfo);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
    }
}
