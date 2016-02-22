package com.idsmanager.eagleeye.ui.fragment.addnewsfragments;

import android.view.View;
import android.widget.LinearLayout;

import com.baidu.mapapi.model.LatLng;
import com.idsmanager.eagleeye.R;
import com.idsmanager.eagleeye.constants.Constants;
import com.idsmanager.eagleeye.ui.fragment.BaseFragment;

/**
 * Created by wind on 2016/1/22.
 */
public class RiversFragment extends BaseFragment {

    private LinearLayout riversFragment;
    private LatLng latLng;


    @Override
    protected View createAddNewsView() {
        riversFragment = (LinearLayout) View.inflate(getActivity(), R.layout.fragment_rivers_add, null);
        return riversFragment;
    }

    @Override
    protected void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    protected void setTitle(String title) {
    }

    @Override
    protected Object loadData() {

        return Constants.ADD_PAGE;
    }
}
