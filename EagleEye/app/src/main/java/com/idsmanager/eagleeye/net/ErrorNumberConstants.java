package com.idsmanager.eagleeye.net;


import com.idsmanager.eagleeye.R;
import com.idsmanager.eagleeye.application.EagleApplication;

/**
 * Created by 雅麟 on 2015/6/19.
 */
public enum ErrorNumberConstants {
    Success(0, "success"),

    ServerError(888, EagleApplication.getContext().getString(R.string.server_error)),
    NetworkError(999, EagleApplication.getContext().getString(R.string.net_word_error_info)),

    UnknownError(9999, EagleApplication.getContext().getString(R.string.unknown_error));

    public final int number;
    public final String msg;

    ErrorNumberConstants(int number, String msg) {
        this.number = number;
        this.msg = msg;
    }

    public static ErrorNumberConstants getValue(int error) {
        for (ErrorNumberConstants item : values()) {
            if (error == item.number) {
                return item;
            }
        }
        return UnknownError;
    }
}
