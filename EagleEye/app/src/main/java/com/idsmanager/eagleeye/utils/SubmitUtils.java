package com.idsmanager.eagleeye.utils;

import com.idsmanager.eagleeye.application.EagleApplication;
import com.idsmanager.eagleeye.domain.Thing;

/**
 * Created by wind on 2016/1/22.
 */
public class SubmitUtils {
    public static boolean submitData(String url, Thing thing) {
        if (thing != null) {
            //saveData(url);
//            SharedPreferencesUtils.setParam(EagleApplication.getContext(), "NAME", thing.name);
//            SharedPreferencesUtils.setParam(EagleApplication.getContext(), "TYPE", thing.type);
//            SharedPreferencesUtils.setParam(EagleApplication.getContext(), "POSITION", thing.position);
//            SharedPreferencesUtils.setParam(EagleApplication.getContext(), "LATITUDE", thing.latLng.latitude);
//            SharedPreferencesUtils.setParam(EagleApplication.getContext(), "LONGITUDE", thing.latLng.latitude);
            return true;
        }
        return false;
    }
}
