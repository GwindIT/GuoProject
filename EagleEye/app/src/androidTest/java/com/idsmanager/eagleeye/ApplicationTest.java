package com.idsmanager.eagleeye;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import junit.framework.Test;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test() {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
// sourceLatLng待转换坐标
        LatLng latLng = new LatLng(39.865329, 116.351952);
        converter.coord(latLng);
        LatLng desLatLng = converter.convert();
        int x = (int) desLatLng.latitudeE6;
        int y = (int) desLatLng.longitudeE6;
        Log.i("转换坐标：", desLatLng + "");

    }
}