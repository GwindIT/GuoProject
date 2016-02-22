package com.idsmanager.eagleeye.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.idsmanager.eagleeye.R;
import com.idsmanager.eagleeye.application.EagleApplication;
import com.idsmanager.eagleeye.constants.Constants;
import com.idsmanager.eagleeye.domain.PatrolNews;
import com.idsmanager.eagleeye.net.IDsManagerPostRequest;
import com.idsmanager.eagleeye.net.NetService;
import com.idsmanager.eagleeye.net.RequestQueueHelper;
import com.idsmanager.eagleeye.net.response.MyStringResponse;
import com.idsmanager.eagleeye.utils.ToastUtil;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String THINGID = "thing";
    public static final String TIME = "time";
    public static final String TYPE = "type";
    public static final String DETAIL = "detail";
    protected String markPointId;
    protected String time;
    protected EditText etAddTime;
    protected EditText etAddType;
    protected EditText etAddDetail;
    protected Button btAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        markPointId = getIntent().getStringExtra(THINGID);
        time = getIntent().getStringExtra(TIME);
        initView();

    }

    private void initView() {
        etAddTime = (EditText) findViewById(R.id.et_search_add);
        etAddType = (EditText) findViewById(R.id.et_search_add_type);
        etAddDetail = (EditText) findViewById(R.id.et_search_add_detail);
        btAdd = (Button) findViewById(R.id.bt_inter_add);
        btAdd.setOnClickListener(this);
        etAddTime.setText(time);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_inter_add:
                PatrolNews patrolNews = new PatrolNews();
                patrolNews.patrolType = etAddType.getText().toString();
                patrolNews.detial = etAddDetail.getText().toString();
                RequestQueue requestQueue = RequestQueueHelper.getInstance(getApplicationContext());
                IDsManagerPostRequest<MyStringResponse> request = new IDsManagerPostRequest<>(NetService.ADD_MARK_DETAIL_URL, MyStringResponse.class, null, NetService.addDetail(markPointId, patrolNews.patrolType, patrolNews.detial),
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                addSuccess(((MyStringResponse) response).detail);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        addFailed(error.getMessage());
                        ToastUtil.showToast("add failed");
                    }
                });

                RetryPolicy retryPolicy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                request.setRetryPolicy(retryPolicy);
                requestQueue.add(request);
                break;
            default:
                break;
        }
    }

    private void addSuccess(String markPointId) {
        ToastUtil.showToast("Add Success");
        LocalBroadcastManager.getInstance(EagleApplication.getContext()).sendBroadcast(new Intent(Constants.ACTION_LOCAL_SEND));
        finish();
    }

    private void addFailed(String msg) {
        ToastUtil.showToast(msg);
    }
}
