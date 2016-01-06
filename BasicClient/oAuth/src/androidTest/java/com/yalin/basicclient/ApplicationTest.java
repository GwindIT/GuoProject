package com.idsmanager.basicclient;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.idsmanager.ssosublibrary.RpSSOApi;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void testGetFacetId(){
        String facetId = RpSSOApi.getFacetId(getContext());
        System.out.println(facetId);
    }
}