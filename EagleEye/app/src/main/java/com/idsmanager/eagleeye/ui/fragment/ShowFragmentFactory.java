package com.idsmanager.eagleeye.ui.fragment;

import com.baidu.mapapi.model.LatLng;
import com.idsmanager.eagleeye.ui.fragment.shownewfragments.ShowTreeFragment;

public class ShowFragmentFactory {
    public static BaseFragment createShowFragment(int position, LatLng latLng,String title) {
        BaseFragment fragment = null;
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new ShowTreeFragment();
                    fragment.setLatLng(latLng);
                    fragment.setTitle(title);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
        }
        return fragment;
    }
}
