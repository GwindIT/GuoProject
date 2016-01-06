package com.idsmanager.oauthclient.net;


import com.android.volley.Response;
import com.idsmanager.oauthclient.net.response.BaseResponse;

import java.util.Map;

/**
 * Created by 雅麟 on 2015/3/21.
 */
public class IDsManagerGetRequest<T extends BaseResponse> extends IDsManagerBaseRequest<T> {
    public IDsManagerGetRequest(String url, Class<T> cls, Map<String, String> header, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, cls, header, listener, errorListener);
    }
    public IDsManagerGetRequest(String url, Class<T> cls, Response.Listener listener, Response.ErrorListener errorListener) {
        this(url, cls, null, listener, errorListener);
    }
}