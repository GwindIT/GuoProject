package com.idsmanager.oauthclient.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idsmanager.oauthclient.R;
import com.idsmanager.oauthclient.constants.AnimationConstants;
import com.idsmanager.oauthclient.module.User;


/**
 * Created by YaLin on 2015/7/24.
 */
public abstract class BaseDrawerActivity extends BaseActivity implements OnClickListener, OnNavigationItemSelectedListener {
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;

    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected LinearLayout llHeaderRoot;
    protected NavigationView navigationView;

    private DrawerHeaderViewHolder drawerHeaderViewHolder;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base_drawer);
        View view = View.inflate(this, layoutResID, null);
        findView();

        FrameLayout content = (FrameLayout) findViewById(R.id.base_content);
        content.addView(view);
        setupView();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MenuItem checkedItem = navigationView.getMenu().findItem(getDrawerCheckId());
        checkedItem.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.base_dl);
        llHeaderRoot = (LinearLayout) findViewById(R.id.me_ll_root);
        drawerHeaderViewHolder = new DrawerHeaderViewHolder(llHeaderRoot);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
    }

    private void setupView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        initDrawerView();
    }


    @Override
    protected void onLoginStateChanged() {
        super.onLoginStateChanged();
        initDrawerView();
    }

    protected abstract int getDrawerCheckId();


    private void initDrawerView() {
        if (User.isLogin(getApplicationContext())) {
            drawerHeaderViewHolder.tvPhone.setText(User.getUserAccount(getApplicationContext()));
            drawerHeaderViewHolder.llHasLogin.setVisibility(View.VISIBLE);
            drawerHeaderViewHolder.llNotLogin.setVisibility(View.GONE);
        } else {
            drawerHeaderViewHolder.llNotLogin.setVisibility(View.VISIBLE);
            drawerHeaderViewHolder.llHasLogin.setVisibility(View.GONE);
        }
        Glide.with(this)
                .load(R.drawable.user)
                .override(150, 150)
                .into(drawerHeaderViewHolder.ivProfile);
        drawerHeaderViewHolder.btnLogin.setOnClickListener(this);
        drawerHeaderViewHolder.btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_btn_login:
                drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AccountActivity.requestLogin(BaseDrawerActivity.this, new int[]{0, 0});
                    }
                }, NAVDRAWER_LAUNCH_DELAY);

                break;
            case R.id.me_btn_logout:
                User.deleteUserInfo(getApplicationContext());
                animateLogout();
                break;
        }
    }

    private void animateLogout() {
        int height = drawerHeaderViewHolder.llRoot.getHeight();
        drawerHeaderViewHolder.ivProfile.setTranslationY(-height);
        drawerHeaderViewHolder.llLogin.setTranslationY(-height);
        drawerHeaderViewHolder.ivProfile.animate().translationY(0)
                .setDuration(AnimationConstants.ANIM_DURATION)
                .setStartDelay(100)
                .setInterpolator(INTERPOLATOR);

        drawerHeaderViewHolder.llLogin.animate().translationY(0)
                .setDuration(AnimationConstants.ANIM_DURATION)
                .setStartDelay(200)
                .setInterpolator(INTERPOLATOR).start();
    }

    private void goToNavDrawerItem(int itemId) {
        switch (itemId) {
            case R.id.navigation_home_page:
                MainActivity.open(this);
                finish();
                break;
            case R.id.navigation_setting:
                createBackStack(new Intent(this, SettingActivity.class));
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNavDrawerItem(menuItem.getItemId());
            }
        }, NAVDRAWER_LAUNCH_DELAY);
        View mainContent = findViewById(R.id.base_dl);
        if (mainContent != null) {
            mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }
        drawerLayout.closeDrawer(Gravity.LEFT);
        return false;
    }

    private void createBackStack(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder builder = TaskStackBuilder.create(this);
            builder.addNextIntentWithParentStack(intent);
            builder.startActivities();
        } else {
            startActivity(intent);
            finish();
        }
    }

    static class DrawerHeaderViewHolder {
        View itemView;
        LinearLayout llRoot;
        LinearLayout llLogin;
        LinearLayout llHasLogin;
        LinearLayout llNotLogin;
        ImageView ivProfile;
        Button btnLogin;
        Button btnLogout;
        TextView tvPhone;

        public DrawerHeaderViewHolder(View view) {
            itemView = view;
            initView(view);
        }

        void initView(View view) {
            llRoot = (LinearLayout) view.findViewById(R.id.me_ll_root);
            llLogin = (LinearLayout) view.findViewById(R.id.setting_ll_login);
            llHasLogin = (LinearLayout) view.findViewById(R.id.setting_ll_has_login);
            llNotLogin = (LinearLayout) view.findViewById(R.id.setting_ll_not_login);
            ivProfile = (ImageView) view.findViewById(R.id.setting_iv_profile);
            btnLogin = (Button) view.findViewById(R.id.me_btn_login);
            btnLogout = (Button) view.findViewById(R.id.me_btn_logout);
            tvPhone = (TextView) view.findViewById(R.id.me_tv_phone);
        }
    }
}
