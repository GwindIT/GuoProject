package com.idsmanager.basicclient.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idsmanager.basicclient.R;

/**
 * Created by wind on 2016/1/5.
 */
public class RegisterFragment extends BaseFragment {
    private static final String TAG = RegisterFragment.class.getSimpleName();
    private RegisterSuccessCallback callback;

    public interface RegisterSuccessCallback {
        void onRegisterSuccess();
    }

    public static RegisterFragment getInstance(RegisterSuccessCallback callback) {
        RegisterFragment fragment = new RegisterFragment();
        fragment.callback = callback;
        return fragment;
    }

    public static void open(int container, FragmentManager manager, RegisterSuccessCallback callback) {
        if (manager.findFragmentByTag(TAG) != null) {
            return;
        }
        manager.beginTransaction().setCustomAnimations(
                R.anim.push_left_in,
                R.anim.push_left_out,
                R.anim.push_right_in,
                R.anim.push_right_out)
                .add(container, getInstance(callback), TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    protected String getRequestTag() {
        return null;
    }
}
