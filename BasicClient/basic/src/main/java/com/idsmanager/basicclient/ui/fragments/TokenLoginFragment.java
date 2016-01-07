package com.idsmanager.basicclient.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.idsmanager.ssosublibrary.RpSSOApi;
import com.idsmanager.ssosublibrary.data.RPToken;
import com.idsmanager.ssosublibrary.interfaces.TokenResultCallback;
import com.jin.uitoolkit.util.Utils;
import com.idsmanager.basicclient.R;
import com.idsmanager.basicclient.module.User;
import com.idsmanager.basicclient.net.IDsManagerGetRequest;
import com.idsmanager.basicclient.net.NetService;
import com.idsmanager.basicclient.net.RequestQueueHelper;
import com.idsmanager.basicclient.net.response.UserResponse;
import com.idsmanager.basicclient.utils.ErrorCodeParser;
import com.idsmanager.basicclient.utils.SnackBarUtil;
import com.idsmanager.basicclient.utils.StatLog;
import com.idsmanager.basicclient.utils.ToastUtil;

/**
 * Created by 雅麟 on 2015/3/22.
 */
public class TokenLoginFragment extends BaseFragment implements View.OnClickListener, TokenResultCallback {
    public interface LoginSuccessCallback {
        void onLoginSuccess();
    }

    private static final String TAG = TokenLoginFragment.class.getSimpleName();

    String token;

    EditText etAccount;
    EditText etServer;
    Button btnLogin;
    Button btnConfirmLogin;

    TextInputLayout tilAccount;
    TextInputLayout tilServer;

    private LoginSuccessCallback callback;
    private String mUsername;

    public static TokenLoginFragment getInstance(LoginSuccessCallback callback) {
        TokenLoginFragment fragment = new TokenLoginFragment();
        fragment.callback = callback;
        return fragment;
    }

    public static void open(int container, FragmentManager manager, LoginSuccessCallback callback) {
        if (manager.findFragmentByTag(TAG) != null) {
            return;
        }
        manager.beginTransaction().setCustomAnimations(
                R.anim.push_left_in,
                R.anim.push_left_out,
                R.anim.push_right_in,
                R.anim.push_right_out)
                .add(container, getInstance(callback), TAG)
                .addToBackStack(null)
                .commit();
    }


    private void initView(View view) {
        etAccount = (EditText) view.findViewById(R.id.token_login_et_account);
        etServer = (EditText) view.findViewById(R.id.token_login_et_server);

        btnLogin = (Button) view.findViewById(R.id.token_login_btn_login);
        btnConfirmLogin = (Button) view.findViewById(R.id.token_login_btn_confirm_login);
        btnLogin.setOnClickListener(this);
        btnConfirmLogin.setOnClickListener(this);

        tilServer = (TextInputLayout) view.findViewById(R.id.token_login_til_server);
        tilAccount = (TextInputLayout) view.findViewById(R.id.token_login_til_account);
        this.etServer.setText(NetService.HTTP_URL);

    }

    @Override
    public void onStart() {
        super.onStart();
        etAccount.post(new Runnable() {
            @Override
            public void run() {
                etAccount.requestFocus();
            }
        });
    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_login, container, false);
        initView(view);
        return view;
    }

    @Override
    protected String getRequestTag() {
        return TokenLoginFragment.class.getName();
    }

    @Override
    public void onClick(View v) {
        mUsername = etAccount.getText().toString().trim();
        switch (v.getId()) {
            case R.id.token_login_btn_login:
                Utils.closeInput(getActivity());
                requestTokenLogin(true, mUsername);
                break;
            case R.id.token_login_btn_confirm_login:
                requestTokenLogin(false, mUsername);
                break;
        }
    }

    public void requestTokenLogin(boolean silence, String username) {
        StatLog.printLog(TAG, getActivity().getLocalClassName());
        if (TextUtils.isEmpty(mUsername)) {
            SnackBarUtil.showSnack(getActivity(), R.string.username_empty);
            return;
        }
        NetService.storeHttpUrl(getActivity().getApplicationContext(), this.etServer.getText().toString().trim());
        RpSSOApi.requestTokenShareData(getActivity(),username,this);
//        String appName = getString(R.string.app_name);
//        if (silence) {
//            if (!RpSSOApi.requestToken(getActivity(), username, appName, this)) {
//                ToastUtil.showToast(getActivity(), R.string.no_app_installed);
//            }
//        } else {
//            if (!RpSSOApi.requestTokenWithConfirm(getActivity(), username, appName, this)) {
//                ToastUtil.showToast(getActivity(), R.string.no_app_installed);
//            }
//        }
    }

    @Override
    public void requestSuccess(RPToken rpToken) {
        this.token = rpToken.getToken();
        rpLogin(token);
    }

    @Override
    public void requestError(int errorCode) {
        dismissLoading();
        SnackBarUtil.showSnack(getActivity(), ErrorCodeParser.parseError(errorCode));
    }

    private void rpLogin(String token) {
        if (TextUtils.isEmpty(mUsername)) {
            ToastUtil.showToast(getActivity().getApplicationContext(), R.string.has_no_token);
            return;
        }
        RequestQueue requestQueue = RequestQueueHelper.getInstance(getActivity());
        IDsManagerGetRequest<UserResponse> request = new IDsManagerGetRequest<>(NetService.RP_TOKEN_LOGIN_URL, UserResponse.class, NetService.getHostAuthHeader(token),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        ToastUtil.showToast(getActivity(), R.string.rp_login_success);
                        User user = ((UserResponse) response).detail;
                        loginSuccess(user);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //ToastUtil.showToast(getActivity(), R.string.rp_login_failed);
                        loginFailed("Sorry,Login Failed");
                    }
                });
        request.setTag(TAG);
        requestQueue.add(request);
    }

    private void loginSuccess(User user) {
        if (user == null) {
            SnackBarUtil.showSnack(getActivity(), getString(R.string.get_userinfo_failed));
            return;
        }
        User.storeUserInfo(getActivity().getApplicationContext(), user);
        if (callback != null) {
            callback.onLoginSuccess();
        }
        if (getActivity() == null) {
            return;
        }
        dismissLoading();
        SnackBarUtil.showSnack(getActivity(), R.string.login_success);
        getActivity().finish();
    }

    private void loginFailed(String msg) {
        if (getActivity() == null) {
            return;
        }
        dismissLoading();
        SnackBarUtil.showSnack(getActivity(), msg);
    }

    protected void onHttpUrlChanged() {
        String str = NetService.getHttpUrl(getActivity().getApplicationContext());
        this.etServer.setText(str);
    }
}
