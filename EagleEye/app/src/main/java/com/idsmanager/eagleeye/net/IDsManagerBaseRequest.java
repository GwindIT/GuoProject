package com.idsmanager.eagleeye.net;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.idsmanager.eagleeye.net.response.BaseResponse;
import com.idsmanager.eagleeye.utils.StatLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by 雅麟 on 2015/6/17.
 */
public class IDsManagerBaseRequest<T extends BaseResponse> extends Request<T> {
    private static final String TAG = "IDsManagerBaseRequest";
    protected static final String ERROR_NUM_KEY = "errorNum";

    protected Response.Listener<T> mListener;
    protected Gson mGson;
    protected Class<T> mCls;
    protected Map<String, String> mHeaders;

    protected IDsManagerBaseRequest(int method, String url, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
    }

    public IDsManagerBaseRequest(int method, String url, Class<T> cls, Response.Listener listener, Response.ErrorListener errorListener) {
        this(method, url, cls, null, listener, errorListener);
    }

    public IDsManagerBaseRequest(int method, String url, Class<T> cls, Map<String, String> headers, Response.Listener listener, Response.ErrorListener errorListener) {
        this(method, url, errorListener);
        mListener = listener;
        mGson = new Gson();
        mCls = cls;
        mHeaders = headers;
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        T parsedGSON;
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            StatLog.printLog(TAG, jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            int error = jsonObject.getInt(ERROR_NUM_KEY);

            ErrorNumberConstants errorConstants = ErrorNumberConstants.getValue(error);
            if (errorConstants == ErrorNumberConstants.Success) {
                parsedGSON = mGson.fromJson(jsonString, mCls);
            } else {
                return Response.error(new MyVolleyError(errorConstants));
            }
        } catch (JSONException e) {
            StatLog.printLog(TAG, "JSONException: " + e.getMessage());
            return Response.error(new MyVolleyError(e, ErrorNumberConstants.ServerError));
        } catch (Exception e) {
            StatLog.printLog(TAG, "UnknownException: " + e.getMessage());
            return Response.error(new MyVolleyError(e, ErrorNumberConstants.ServerError));
        }

        return Response.success(parsedGSON, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError != null) {
            if (volleyError.networkResponse == null) {
                StatLog.printLog(TAG, "parseNetworkError: " + volleyError.getMessage());
                volleyError = new MyVolleyError(ErrorNumberConstants.NetworkError);
            } else {
                volleyError = new MyVolleyError(ErrorNumberConstants.ServerError);
            }
        }
        return super.parseNetworkError(volleyError);
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeaders != null && mHeaders.size() > 0) {
            return mHeaders;
        }
        return super.getHeaders();
    }
}
