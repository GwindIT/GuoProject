package com.idsmanager.eagleeye.net;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * Created by YaLin on 2015/7/20.
 */
public class MyVolleyError extends VolleyError {
    public ErrorNumberConstants errorConstant;

    public MyVolleyError(ErrorNumberConstants errorConstant) {
        super(errorConstant.msg);
        this.errorConstant = errorConstant;
    }

    public MyVolleyError(NetworkResponse response, ErrorNumberConstants errorConstant) {
        super(response);
        this.errorConstant = errorConstant;
    }

    public MyVolleyError(String exceptionMessage, ErrorNumberConstants errorConstant) {
        super(exceptionMessage);
        this.errorConstant = errorConstant;
    }

    public MyVolleyError(String exceptionMessage, Throwable reason, ErrorNumberConstants errorConstant) {
        super(exceptionMessage, reason);
        this.errorConstant = errorConstant;
    }

    public MyVolleyError(Throwable cause, ErrorNumberConstants errorConstant) {
        super(cause);
        this.errorConstant = errorConstant;
    }
}
