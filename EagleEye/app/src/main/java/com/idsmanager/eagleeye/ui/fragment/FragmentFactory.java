package com.idsmanager.eagleeye.ui.fragment;

/**
 * 需求发生了改变，添加类型不再使用。但让仍然保留以防需求再次改变。
 */
import com.baidu.mapapi.model.LatLng;
import com.idsmanager.eagleeye.ui.fragment.addnewsfragments.RiversFragment;
import com.idsmanager.eagleeye.ui.fragment.addnewsfragments.TreeFragment;

public class FragmentFactory {
    public static BaseFragment createFragment(int position, LatLng latLng) {
        BaseFragment fragment = null;
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new TreeFragment();
                    fragment.setLatLng(latLng);
                    break;
                case 1:
                    fragment = new RiversFragment();
                    fragment.setLatLng(latLng);
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
