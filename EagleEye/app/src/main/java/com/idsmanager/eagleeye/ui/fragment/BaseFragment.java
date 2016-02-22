package com.idsmanager.eagleeye.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.model.LatLng;
import com.idsmanager.eagleeye.application.EagleApplication;
import com.idsmanager.eagleeye.utils.ViewUtils;

public abstract class BaseFragment extends Fragment {
    protected ContentPage contentPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentPage == null) {
            contentPage = new ContentPage(getActivity()) {
                @Override
                protected View createNewsView() {
                    return BaseFragment.this.createAddNewsView();
                }

                @Override
                protected Object loadData() {
                    return BaseFragment.this.loadData();
                }
            };
        } else {
            ViewUtils.removeSelfFromParent(contentPage);
        }
        return contentPage;
    }

    protected abstract View createAddNewsView();

    protected abstract Object loadData();

    protected abstract void setLatLng(LatLng latLng);

    protected abstract void setTitle(String title);

    protected void runOnUIThread(Runnable runnable) {
        EagleApplication.getMainHandler().post(runnable);
    }
}
