package com.idsmanager.eagleeye.net;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.idsmanager.eagleeye.application.EagleApplication;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 雅麟 on 2015/3/19.
 */
public class NetService {
    static {
        init(EagleApplication.getContext());
    }

    private static final String HTTP_SP = "http_url";
    private static final String HTTP_KEY = "http";

    public static String HTTP_URL;

    public static final String BASE_URL = "http://123.59.78.13:8888";

    public static String ADD_MARK_URL;
    public static String ADD_MARK_DETAIL_URL;
    public static String FIND_MARK_URL;
    public static String FIND_ALL_MARK_URL;
    public static String FIND_DETAIL_URL;
    public static String GET_IMAGES_URL;


    private static final String ADD_MARK_SUB = "/Map/api/markpoint/create";
    private static final String ADD_MARK_DETAIL_SUB = "/Map/api/patrol/create";
    private static final String FIND_MARK_SUB = "/Map/api/markpoint/latitudeLongitude ";
    private static final String FIND_ALL_MARK_SUB = "/Map/api/markpoint/all";
    private static final String FIND_DETAIL_SUB = "/Map/api/patrol/detail";
    private static final String GET_IMAGES_SUB = "/Map/public/image";


    public static String getHttpUrl(Context context) {
        if (TextUtils.isEmpty(HTTP_URL)) {
            SharedPreferences sp = context.getSharedPreferences(
                    HTTP_SP, Context.MODE_PRIVATE);
            HTTP_URL = sp.getString(HTTP_KEY, BASE_URL);
        }
        return HTTP_URL;
    }

    public static void storeHttpUrl(Context context, String url) {
        SharedPreferences sp = context.getSharedPreferences(
                HTTP_SP, Context.MODE_PRIVATE);
        HTTP_URL = url;
        sp.edit().putString(HTTP_KEY, HTTP_URL)
                .apply();
        init(context);
    }

    private static void init(Context context) {
        getHttpUrl(context);
        ADD_MARK_URL = HTTP_URL + ADD_MARK_SUB;
        ADD_MARK_DETAIL_URL = HTTP_URL + ADD_MARK_DETAIL_SUB;
        FIND_ALL_MARK_URL = HTTP_URL + FIND_ALL_MARK_SUB;
        FIND_MARK_URL = HTTP_URL + FIND_MARK_SUB;
        FIND_DETAIL_URL = HTTP_URL + FIND_DETAIL_SUB;
        GET_IMAGES_URL = HTTP_URL + GET_IMAGES_SUB;
    }

    public static Map<String, String> addNewMark(String pointName, String pointType, String pointPosition, double latitude, double longitude, String layer, String imgs) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("pointName", pointName);
        params.put("pointType", pointType);
        params.put("pointPosition", pointPosition);
        params.put("latitude", latitude + "");
        params.put("longitude", longitude + "");
        params.put("layer", layer);
        if (!TextUtils.isEmpty(imgs)) {
            params.put("imgs", imgs);
        }
        return params;
    }

    public static Map<String, String> getMark(double latitude, double longitude) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("latitude", latitude + "");
        params.put("longitude", longitude + "");
        return params;
    }

    public static Map<String, String> add() {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("", "");
        return params;
    }

    public static Map<String, String> addDetail(String markPointId, String patrolType, String detial) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("markPointId", markPointId);
        params.put("patrolType", patrolType);
        params.put("detial", detial);
        return params;
    }

    public static Map<String, String> getDetail(String markPointId) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("markPointId", markPointId);
        return params;
    }

}
