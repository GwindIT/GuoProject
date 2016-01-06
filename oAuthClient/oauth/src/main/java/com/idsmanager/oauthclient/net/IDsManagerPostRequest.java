package com.idsmanager.oauthclient.net;


import android.support.v4.util.ArrayMap;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.idsmanager.oauthclient.net.response.BaseResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by 雅麟 on 2015/3/21.
 */
public class IDsManagerPostRequest<T extends BaseResponse> extends IDsManagerBaseRequest<T> {
    protected Map<String, String> mParams;

    protected Map<String, JSONArray> mListParams;

    public IDsManagerPostRequest(String url, Class<T> cls, Map<String, String> header, Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, cls, header, listener, errorListener);
        mParams = params;
    }

    public IDsManagerPostRequest(String url, Class<T> cls, Map<String, String> params, Response.Listener listener, Response.ErrorListener errorListener) {
        this(url, cls, null, params, listener, errorListener);
    }

    public void addListParams(String key, JSONArray array) {
        if (mListParams == null) {
            mListParams = new ArrayMap<>();
        }
        mListParams.put(key, array);
    }

    public void addListParams(Map<String, JSONArray> arrayMap) {
        if (mListParams == null) {
            mListParams = new ArrayMap<>();
        }
        mListParams.putAll(arrayMap);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            JSONObject obj = new JSONObject();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                obj.put(entry.getKey(), entry.getValue());
            }
            encodedParams.append(obj);
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        } catch (JSONException e) {
            throw new RuntimeException("Json Exception " + paramsEncoding, e);
        }
    }
}