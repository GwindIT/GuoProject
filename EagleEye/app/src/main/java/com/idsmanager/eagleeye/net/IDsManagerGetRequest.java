package com.idsmanager.eagleeye.net;

import com.android.volley.Response;
import com.idsmanager.eagleeye.net.response.BaseResponse;

import java.util.Map;

/**
 * Created by 雅麟 on 2015/3/21.
 */
public class IDsManagerGetRequest<T extends BaseResponse> extends IDsManagerBaseRequest<T> {

    public IDsManagerGetRequest(String url, Class<T> cls, Response.Listener listener, Response.ErrorListener errorListener) {
        this(url, cls, null, listener, errorListener);
    }

    public IDsManagerGetRequest(String url, Class<T> cls, Map<String, String> headers, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, cls, headers, listener, errorListener);
    }

}