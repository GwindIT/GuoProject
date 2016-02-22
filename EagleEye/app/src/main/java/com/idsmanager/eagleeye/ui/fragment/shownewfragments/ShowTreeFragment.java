package com.idsmanager.eagleeye.ui.fragment.shownewfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.model.LatLng;
import com.idsmanager.eagleeye.R;
import com.idsmanager.eagleeye.adapter.ImagesAdapter;
import com.idsmanager.eagleeye.adapter.PatrolAdapter;
import com.idsmanager.eagleeye.application.EagleApplication;
import com.idsmanager.eagleeye.constants.Constants;
import com.idsmanager.eagleeye.domain.PatrolNews;
import com.idsmanager.eagleeye.domain.Patrols;
import com.idsmanager.eagleeye.domain.Thing;
import com.idsmanager.eagleeye.net.IDsManagerGetRequest;
import com.idsmanager.eagleeye.net.IDsManagerPostRequest;
import com.idsmanager.eagleeye.net.NetService;
import com.idsmanager.eagleeye.net.RequestQueueHelper;
import com.idsmanager.eagleeye.net.response.ImagesResponse;
import com.idsmanager.eagleeye.net.response.MyJsonResponse;
import com.idsmanager.eagleeye.net.response.MyStringResponse;
import com.idsmanager.eagleeye.net.response.PatrolResponse;
import com.idsmanager.eagleeye.ui.activity.SearchActivity;
import com.idsmanager.eagleeye.ui.fragment.BaseFragment;
import com.idsmanager.eagleeye.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wind on 2016/1/23.
 */
public class ShowTreeFragment extends BaseFragment implements View.OnClickListener {
    private LatLng latLng;
    private String name;
    protected String type;
    protected String position;
    protected long latitude;
    protected long longitude;
    protected String markId;
    private ScrollView showTreeFragment;
    protected ViewPager viewPager;

    protected ImageButton ibButton;
    protected ListView lsView;
    private TextView tvName;
    private TextView tvPosition;
    private TextView tvLatitude;
    private TextView tvLongitude;
    protected List<String> imagesId;
    protected TextView tvShowNo;
    private Thing mThing;
    private PatrolAdapter adapter;
    private RequestQueue requestQueue;
    public ChangeBroadcastReceiver receiver;
    protected List<PatrolNews> list = null;
    protected List<Bitmap> bitmaps = new ArrayList<>();
    protected ImagesAdapter imagesAdapter;

    public static final int REQUEST = 1;

    @Override
    public void onStart() {
        super.onStart();
        receiver = new ChangeBroadcastReceiver();
        handler.sendMessageDelayed(Message.obtain(handler), 3000);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(Constants.ACTION_LOCAL_SEND));
    }

    @Override
    protected View createAddNewsView() {
        initView();
        initData();
        return showTreeFragment;
    }

    @Override
    protected Object loadData() {
        return 1;
    }

    private void initView() {
        showTreeFragment = (ScrollView) View.inflate(getActivity(), R.layout.fragment_show, null);
        tvName = (TextView) showTreeFragment.findViewById(R.id.tv_fragment_name_show);
        tvLatitude = (TextView) showTreeFragment.findViewById(R.id.tv_fragment_show_latitude);
        tvLongitude = (TextView) showTreeFragment.findViewById(R.id.tv_fragment_show_longitude);
        tvPosition = (TextView) showTreeFragment.findViewById(R.id.tv_show_position);
        ibButton = (ImageButton) showTreeFragment.findViewById(R.id.ib_add_problem);
        viewPager = (ViewPager) showTreeFragment.findViewById(R.id.vp_show);
        ibButton.setOnClickListener(this);
        lsView = (ListView) showTreeFragment.findViewById(R.id.ls_problem);

        tvShowNo = (TextView) showTreeFragment.findViewById(R.id.tv_show_no_problem);
        tvShowNo.setOnClickListener(this);
    }

    private void initData() {
        requestQueue = RequestQueueHelper.getInstance(getActivity().getApplicationContext());
        IDsManagerPostRequest<MyJsonResponse> request = new IDsManagerPostRequest<>(NetService.FIND_MARK_URL, MyJsonResponse.class, null, NetService.getMark(latLng.latitude, latLng.longitude),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        getSuccess(((MyJsonResponse) response).detail);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //addFailed(error.getMessage());
            }
        });

        RetryPolicy retryPolicy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        requestQueue.add(request);
    }

    private void getSuccess(Thing thing) {
        mThing = thing;
        name = thing.pointName;
        markId = thing.uuid;
        position = thing.pointPosition;

        imagesId = thing.imgsId;
        tvLatitude.setText(latLng.longitude + "");
        tvLongitude.setText(latLng.latitude + "");
        tvName.setText(name + "(" + thing.pointType + ")");
        tvPosition.setText(position);

        IDsManagerPostRequest<PatrolResponse> request2 = new IDsManagerPostRequest<>(NetService.FIND_DETAIL_URL, PatrolResponse.class, null, NetService.getDetail(markId),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        getPatrolSuccess(((PatrolResponse) response).detail);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showToast(error.getMessage());
            }
        });
        RetryPolicy retryPolicy2 = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request2.setRetryPolicy(retryPolicy2);
        requestQueue.add(request2);

        initImage(imagesId);
    }

    private void initImage(List<String> imagesId) {
        //http://123.59.78.13:8888//Map/public/image/f1bd6b6d34474f119aafef423d155f15
        if (imagesId != null) {
            for (String imageId : imagesId) {
                ImageRequest request4 = new ImageRequest(NetService.GET_IMAGES_URL + "/" + imageId,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                getImageSuccess(response);

                            }
                        }, 0, 0, Bitmap.Config.RGB_565,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ToastUtil.showToast("get images failed");
                            }
                        });
                RetryPolicy retryPolicy4 = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                request4.setRetryPolicy(retryPolicy4);
                requestQueue.add(request4);
            }
        }
    }

    public void getImageSuccess(Bitmap bitmap) {
        if (bitmap != null) {
            bitmaps.add(bitmap);
            imagesAdapter = new ImagesAdapter(EagleApplication.getContext(), bitmaps);
            viewPager.setAdapter(imagesAdapter);
            if (bitmaps.size() != 1) {
                viewPager.setCurrentItem(bitmaps.size() * 1000 * 50);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (viewPager.getWindowVisibility() == View.GONE) {
                handler.removeCallbacksAndMessages(null);
                return;
            }
            int currentItem = viewPager.getCurrentItem();
            if (currentItem == viewPager.getAdapter().getCount() - 1) {
                currentItem = 0;
            } else {
                currentItem++;
            }
            viewPager.setCurrentItem(currentItem);
            MySendMessage();
        }
    };

    public void MySendMessage() {
        handler.sendMessageDelayed(Message.obtain(handler), 3000);
    }

    private void getPatrolSuccess(Patrols patrols) {
        list = patrols.patrols;
        adapter = new PatrolAdapter(getActivity(), list);
        lsView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        fixListViewHeight(lsView);
    }

    public void fixListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listViewItem = listAdapter.getView(i, null, listView);
            listViewItem.measure(0, 0);
            totalHeight += listViewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    protected void setTitle(String title) {
        this.name = title;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_add_problem:
                showAddActivity();
                break;
            case R.id.tv_show_no_problem:
                showAddActivity();
                break;
            default:
                break;
        }
    }

    protected void showAddActivity() {
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View layout2 = inflate.inflate(R.layout.activity_search, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(layout2);
        final Dialog dialog = builder.show();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        final EditText etDate = (EditText) layout2.findViewById(R.id.et_search_add);
        etDate.setText(str);
        final EditText etType = (EditText) layout2.findViewById(R.id.et_search_add_type);
        final EditText etDetail = (EditText) layout2.findViewById(R.id.et_search_add_detail);
        Button btConfirm = (Button) layout2.findViewById(R.id.bt_inter_add);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatrolNews patrolNews = new PatrolNews();
                patrolNews.patrolType = etType.getText().toString();
                patrolNews.detial = etDetail.getText().toString();
                if (!TextUtils.isEmpty(patrolNews.patrolType) && !TextUtils.isEmpty(patrolNews.detial)) {
                    RequestQueue requestQueue = RequestQueueHelper.getInstance(EagleApplication.getContext());
                    IDsManagerPostRequest<MyStringResponse> request = new IDsManagerPostRequest<>(NetService.ADD_MARK_DETAIL_URL, MyStringResponse.class, null, NetService.addDetail(mThing.uuid, patrolNews.patrolType, patrolNews.detial),
                            new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    ToastUtil.showToast("添加成功");
                                    LocalBroadcastManager.getInstance(EagleApplication.getContext()).sendBroadcast(new Intent(Constants.ACTION_LOCAL_SEND));
                                    dialog.dismiss();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ToastUtil.showToast("添加失败，请检查您的网络");
                        }
                    });

                    RetryPolicy retryPolicy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    request.setRetryPolicy(retryPolicy);
                    requestQueue.add(request);
                } else {
                    ToastUtil.showToast("请输入完整信息");
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(handler);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    public class ChangeBroadcastReceiver extends android.content.BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_LOCAL_SEND)) {
                IDsManagerPostRequest<PatrolResponse> request3 = new IDsManagerPostRequest<>(NetService.FIND_DETAIL_URL, PatrolResponse.class, null, NetService.getDetail(markId),
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                getPatrolSuccess(((PatrolResponse) response).detail);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //addFailed(error.getMessage());
                    }
                });
                RetryPolicy retryPolicy3 = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                request3.setRetryPolicy(retryPolicy3);
                requestQueue.add(request3);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
