package com.idsmanager.basicclient.utils;


import com.idsmanager.ssosublibrary.interfaces.ErrorConstants;
import com.idsmanager.basicclient.R;

/**
 * Created by YaLin on 2015/12/2.
 */
public class ErrorCodeParser {
    public static int parseError(int errorCode) {
        switch (errorCode) {
            case ErrorConstants.ERROR_HOST_NOT_LOGIN:
                return R.string.request_rp_token_failed_not_login;
            case ErrorConstants.ERROR_HOST_LOGIN_TIMEOUT:
                return R.string.host_login_timeout;
            case ErrorConstants.ERROR_REQUEST_TOKEN_ILLEGAL:
                return R.string.rp_user_not_belong_host;
            case ErrorConstants.ERROR_GET_TOKEN_SERVER_ERROR:
                return R.string.server_error;
            case ErrorConstants.ERROR_HOST_USER_NOT_EXIST:
                return R.string.user_not_exist;
            case ErrorConstants.ERROR_HOST_PASSWORD_ERROR:
                return R.string.password_error;
            case ErrorConstants.ERROR_HOST_PIN_CODE_ERROR:
                return R.string.pin_code_error;
            case ErrorConstants.ERROR_HOST_PIN_CODE_BOUND:
                return R.string.pin_code_bound;
            case ErrorConstants.ERROR_HOST_DEVICE_ID_ERROR:
                return R.string.device_id_error;
            case ErrorConstants.ERROR_HOST_NOT_BOUND:
                return R.string.host_user_not_bound;
            case ErrorConstants.ERROR_GET_RP_TOKEN_FAILED:
                return R.string.request_token_error;
            default:
                return R.string.unknown_error;
        }
    }
}
