package com.idsmanager.eagleeye.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.idsmanager.eagleeye.R;
import com.idsmanager.eagleeye.adapter.NewImagesAdapter;
import com.idsmanager.eagleeye.net.IDsManagerPostRequest;
import com.idsmanager.eagleeye.net.NetService;
import com.idsmanager.eagleeye.net.RequestQueueHelper;
import com.idsmanager.eagleeye.net.response.MyStringResponse;
import com.idsmanager.eagleeye.utils.ImageUtil;
import com.idsmanager.eagleeye.utils.StatLog;
import com.idsmanager.eagleeye.utils.ToastUtil;

import java.util.List;


public class AddNewsActivity extends BaseLoadActivity implements View.OnClickListener, NewImagesAdapter.ImageItemClickListener {
    private static final String TAG = AddNewsActivity.class.getSimpleName();
    private LatLng latLng;
    private EditText tvPointName;
    private EditText tvPosition;
    private RadioGroup addRadioGroup;
    private RadioButton addRadioButton;
    private RecyclerView rvImages;
    private TextView latitude;
    private TextView longitude;

    private NewImagesAdapter mAdapter;

    protected Button btCreate;
    private final int IMAGE_OPEN = 1;

    public static final String NEWS_NAME = "newsName";
    public static final String News_Type = "newsType";
    public static final String News_LatLng = "newsLatLng";

    public static final String LOCATION_KEY = "latLng";

    private String pathImage;

    private String position;
    private String name;
    private String type;

    public static void open(Activity activity, int fragmentId, LatLng latLng) {
        Intent intent = new Intent(activity, AddNewsActivity.class);
        intent.putExtra(LOCATION_KEY, latLng);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_add_news);
        Bundle extras = getIntent().getExtras();
        latLng = (LatLng) extras.get(LOCATION_KEY);
        initView();
        initData();
    }

    private void initView() {
        initActionBar();
        tvPointName = (EditText) findViewById(R.id.et_add_name);
        tvPosition = (EditText) findViewById(R.id.et_add_position);
        addRadioGroup = (RadioGroup) findViewById(R.id.rg_add_type);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        rvImages = (RecyclerView) findViewById(R.id.rv_images);
        btCreate = (Button) findViewById(R.id.bt_create);
        btCreate.setOnClickListener(this);
        setupRecyclerView();
    }

    private void initData() {
        latitude.setText(String.format(getString(R.string.string_latitude), latLng.latitude + ""));
        longitude.setText(String.format(getString(R.string.string_longitude), latLng.longitude + ""));
        mAdapter = new NewImagesAdapter(this, null, this);
        rvImages.setAdapter(mAdapter);
    }

    public void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvImages.setLayoutManager(gridLayoutManager);
    }

    protected void addPic(String pathImage) {
        mAdapter.addImage(pathImage);
    }

    private void getData() {
        name = tvPointName.getText().toString().trim();
        position = tvPosition.getText().toString().trim();
        addRadioButton = (RadioButton) findViewById(addRadioGroup.getCheckedRadioButtonId());
        type = addRadioButton.getText().toString().trim();
        addRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                addRadioButton = (RadioButton) findViewById(addRadioGroup.getCheckedRadioButtonId());
                type = addRadioButton.getText().toString();
            }
        });
    }

    @Override
    public void onClick(View v) {
        getData();
        switch (v.getId()) {
            case R.id.bt_create:
                if (checkData()) {
                    addNewMark(mAdapter.getImages());
                }
                break;
            default:
                break;
        }
    }


    private boolean checkData() {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(position) || TextUtils.isEmpty(type)) {
            ToastUtil.showToast(getString(com.idsmanager.eagleeye.R.string.name_or_location_is_empty));
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            Uri uri = data.getData();
            pathImage = uri.getPath();
            addPic(pathImage);
        }
    }

    @Override
    public void onItemClicked(View view, String image, int position) {

    }

    @Override
    public void onAddImageClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_OPEN);
    }

    private void addNewMark(List<String> pathList) {
        String layer = "test layer";

        showLoading();
        String imagesBase64 = ImageUtil.getCompressedFilesBase64(pathList);

        RequestQueue requestQueue = RequestQueueHelper.getInstance(getApplicationContext());
        IDsManagerPostRequest<MyStringResponse> request = new IDsManagerPostRequest<>(NetService.ADD_MARK_URL, MyStringResponse.class, null, NetService.addNewMark(name, type, position, latLng.latitude, latLng.longitude, layer, imagesBase64),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        addSuccess((MyStringResponse) response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                addFailed(error.getMessage());
            }
        });

        RetryPolicy retryPolicy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        request.setTag(TAG);
        requestQueue.add(request);
    }

    private void addSuccess(MyStringResponse response) {
        dismissLoading();
        ToastUtil.showToast(response.detail);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
        Intent intent = new Intent();
        intent.putExtra(NEWS_NAME, name);
        intent.putExtra(News_Type, type);
        intent.putExtra(News_LatLng, latLng);
        setResult(RESULT_OK, intent);
    }

    private void addFailed(String msg) {
        dismissLoading();
        ToastUtil.showToast(msg);
    }
}
