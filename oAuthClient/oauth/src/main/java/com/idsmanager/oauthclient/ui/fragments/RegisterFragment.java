package com.idsmanager.oauthclient.ui.fragments;

import android.os.Bundle;
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

import com.idsmanager.oauthclient.R;
import com.idsmanager.oauthclient.module.RegisterResult;
import com.idsmanager.oauthclient.module.User;
import com.idsmanager.oauthclient.net.IDsManagerPostRequest;
import com.idsmanager.oauthclient.net.NetService;
import com.idsmanager.oauthclient.net.RequestQueueHelper;
import com.idsmanager.oauthclient.net.response.RegisterResponse;
import com.idsmanager.oauthclient.net.response.UserResponse;
import com.idsmanager.oauthclient.utils.SnackBarUtil;
import com.jin.uitoolkit.util.Utils;

/**
 * Created by wind on 2016/1/5.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = RegisterFragment.class.getSimpleName();

    EditText mEtAccount;
    EditText mEtPassword;
    Button mBtnRegister;
    EditText etServer;

    private String mUsername;
    private String password;

    private RegisterSuccessCallback callback;

    public interface RegisterSuccessCallback {
        void onRegisterSuccess();
    }

    public static RegisterFragment getInstance(RegisterSuccessCallback callback) {
        RegisterFragment fragment = new RegisterFragment();
        fragment.callback = callback;
        return fragment;
    }

    public static void open(int container, FragmentManager manager, RegisterSuccessCallback callback) {
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

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mEtAccount.post(new Runnable() {
            @Override
            public void run() {
                mEtAccount.requestFocus();
            }
        });
        String server = NetService.getHttpUrl(getActivity().getApplicationContext());
        if (!TextUtils.isEmpty(server)) {
            etServer.setText(server);
        }
    }

    @Override
    protected String getRequestTag() {
        return null;
    }

    void initView(View view) {
        etServer = (EditText) view.findViewById(R.id.register_et_server);
        mEtAccount = (EditText) view.findViewById(R.id.register_et_account);
        mEtPassword = (EditText) view.findViewById(R.id.register_et_password);
        mBtnRegister = (Button) view.findViewById(R.id.register_btn_register);
        mBtnRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn_register:
                Utils.closeInput(getActivity());
                register();
                break;
        }
    }


    private void register() {
        if (!checkData()) {
            return;
        }
        showLoading();

        NetService.storeHttpUrl(getActivity().getApplicationContext(), etServer.getText().toString().trim());
        RequestQueue requestQueue = RequestQueueHelper.getInstance(getActivity().getApplicationContext());
        IDsManagerPostRequest<RegisterResponse> request = new IDsManagerPostRequest<>(NetService.REGISTER_URL, RegisterResponse.class, null, NetService.register(mUsername, password),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        registerSuccess(((RegisterResponse) response).detail);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        registerError(error.getMessage());
                    }
                }
        );
        request.setTag(getRequestTag());
        requestQueue.add(request);
    }


    private boolean checkData() {
        mUsername = mEtAccount.getText().toString().trim();
        password = mEtPassword.getText().toString().trim();
        mUsername = mUsername.replace(" ", "");
        if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(password)) {
            SnackBarUtil.showSnack(getActivity(), R.string.user_is_null);
            return false;
        }
        return true;
    }

    private void registerSuccess(RegisterResult registerResult) {
        if (getActivity() == null) {
            return;
        }
        login(registerResult);
        SnackBarUtil.showSnack(getActivity(), getString(R.string.register_success));

    }

    private void registerError(String errorMsg) {
        if (getActivity() == null) {
            return;
        }
        dismissLoading();
        SnackBarUtil.showSnack(getActivity(), errorMsg);
    }


    private void login(RegisterResult registerResult) {
        if (TextUtils.isEmpty(registerResult.username) && TextUtils.isEmpty(registerResult.password)) {
            SnackBarUtil.showSnack(getActivity(), getString(R.string.register_failed));
            return;
        }
        NetService.storeHttpUrl(getActivity().getApplicationContext(), etServer.getText().toString().trim());
        RequestQueue requestQueue = RequestQueueHelper.getInstance(getActivity().getApplicationContext());
        final IDsManagerPostRequest<UserResponse> request = new IDsManagerPostRequest(NetService.RP_LOGIN_URL, UserResponse.class,
                NetService.rpLogin(registerResult.username, registerResult.password),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        loginSuccess(((UserResponse) response).detail);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginFailed("Login Failed");
                    }
                });
        request.setTag(getRequestTag());
        requestQueue.add(request);
    }

    private void loginSuccess(User user) {
        User.storeUserInfo(getActivity().getApplicationContext(), user);
        if (this.callback != null)
            callback.onRegisterSuccess();
        if (getActivity() == null)
            return;
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

    @Override
    protected void onHttpUrlChanged() {
        String server = NetService.getHttpUrl(getActivity().getApplicationContext());
        etServer.setText(server);
    }
}
