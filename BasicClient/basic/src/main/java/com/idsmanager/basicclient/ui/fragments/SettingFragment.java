package com.idsmanager.basicclient.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idsmanager.ssosublibrary.RpSSOApi;
import com.idsmanager.basicclient.R;
import com.idsmanager.basicclient.module.User;
import com.idsmanager.basicclient.utils.SnackBarUtil;


/**
 * Created by 雅麟 on 2015/3/22.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    public interface SettingItemClickCallback {
        void onPersonalInfoClicked();
    }

    public static final String TAG = SettingFragment.class.getSimpleName();

    RelativeLayout rlPersonalInfo;
    LinearLayout llHasLogin;
    LinearLayout llNotLogin;
    ImageView ivProfile;
    TextView tvPhone;
    Button btWholeLogout;

    private SettingItemClickCallback settingItemClickCallback;


    public static SettingFragment getInstance(SettingItemClickCallback settingItemClickCallback) {
        SettingFragment fragment = new SettingFragment();
        fragment.settingItemClickCallback = settingItemClickCallback;
        return fragment;
    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(view);
        initData();

        return view;
    }

    @Override
    protected String getRequestTag() {
        return SettingFragment.class.getName();
    }

    private void initView(View view) {
        rlPersonalInfo = (RelativeLayout) view.findViewById(R.id.setting_rl_personal_info);
        llHasLogin = (LinearLayout) view.findViewById(R.id.setting_ll_has_login);
        llNotLogin = (LinearLayout) view.findViewById(R.id.setting_ll_not_login);
        ivProfile = (ImageView) view.findViewById(R.id.setting_iv_profile);
        tvPhone = (TextView) view.findViewById(R.id.setting_tv_phone);
        btWholeLogout=(Button) view.findViewById(R.id.setting_btn_whole_logout);

        rlPersonalInfo.setOnClickListener(this);
        btWholeLogout.setOnClickListener(this);
    }

    private void initData() {
        if (User.isLogin(getActivity().getApplicationContext())) {
            tvPhone.setText(User.getUserAccount(getActivity().getApplicationContext()));
            llHasLogin.setVisibility(View.VISIBLE);
            llNotLogin.setVisibility(View.GONE);
        } else {
            llNotLogin.setVisibility(View.VISIBLE);
            llHasLogin.setVisibility(View.GONE);
        }
        Glide.with(getActivity())
                .load(R.drawable.user)
                .override(150, 150)
                .centerCrop()
                .into(ivProfile);
    }
    @Override
    public void onClick(View v) {
        if (settingItemClickCallback == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.setting_rl_personal_info:
                settingItemClickCallback.onPersonalInfoClicked();
                break;
            case R.id.setting_btn_whole_logout:
                RpSSOApi.logoutOverall(getActivity());
                SnackBarUtil.showSnack(getActivity(),R.string.logout_overall_success);
        }
    }
    @Override
    protected void onLoginStateChanged() {
        super.onLoginStateChanged();
        initData();
    }
}
