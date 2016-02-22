package com.idsmanager.eagleeye.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.idsmanager.eagleeye.R;
import com.idsmanager.eagleeye.domain.Tree;
import com.idsmanager.eagleeye.ui.fragment.BaseFragment;
import com.idsmanager.eagleeye.ui.fragment.ShowFragmentFactory;

public class ShowNewsActivity extends AppCompatActivity {

    private int fragmentType;
    private LatLng latLng;
    private String name;

    public static void open(Activity activity, LatLng latLng, String name, int fragmentType) {
        Intent intent = new Intent(activity, ShowNewsActivity.class);
        intent.putExtra("fragmentType", fragmentType);
        intent.putExtra("latLng", latLng);
        intent.putExtra("title", name);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("title");
        latLng = (LatLng) extras.get("latLng");
        fragmentType = extras.getInt("fragmentType");
        initView();
    }

    private void initView() {
        initToolbar();
        FragmentManager manager = getSupportFragmentManager();
        BaseFragment baseFragment = ShowFragmentFactory.createShowFragment(fragmentType, latLng, name);
        manager.beginTransaction().replace(R.id.fl_show, baseFragment).commit();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_show_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }
}
