package com.idsmanager.eagleeye.ui.fragment;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.idsmanager.eagleeye.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class ContentPage extends RelativeLayout {
    /**
     * 页面状态枚举
     */
    public enum PageState {
        STATE_LOADING(1), STATE_NEWS(2), STATE_ERROR(3);
        private int value;

        PageState(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private PageState mState = PageState.STATE_LOADING;//当前页面的状态，默认初始化为loading状态，
    private View loadingView;//加载状态的view
    private View errorView;//加载\提交错误的view
    private View newsView;//加载添加信息view
    public LayoutParams params;


    public ContentPage(Context context) {
        super(context);
        initPage();
    }


    private void initPage() {
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (loadingView == null) {
            loadingView = View.inflate(getContext(), R.layout.page_loading, null);
            addView(loadingView, params);
        }
        if (errorView == null) {
            errorView = View.inflate(getContext(), R.layout.page_error, null);
            Button button = (Button) errorView.findViewById(R.id.btn_reload);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mState = PageState.STATE_LOADING;
                    showPage();
                    loadDateAndRefreshView();
                }
            });
            addView(errorView, params);
        }
        if (newsView == null) {
            newsView = createNewsView();
            if (newsView != null) {
                addView(newsView, params);
            } else {
                throw new IllegalArgumentException("this can not be null");
            }
        }
        loadDateAndRefreshView();
        showPage();
    }

    private void loadDateAndRefreshView() {
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //模拟延时加载
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Object loadResult = loadData();
                if (loadResult != null) {
                    mState = PageState.STATE_NEWS;
                } else {
                    mState = PageState.STATE_ERROR;
                }
                handler.sendEmptyMessage(MSG_REFRESH_VIEW);
            }
        });
    }

    private void showPage() {
        loadingView.setVisibility(mState == PageState.STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        errorView.setVisibility(mState == PageState.STATE_ERROR ? View.VISIBLE : View.INVISIBLE);
        newsView.setVisibility(mState == PageState.STATE_NEWS ? View.VISIBLE : View.INVISIBLE);
    }

    private final int MSG_REFRESH_VIEW = 1000;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_VIEW:
                    showPage();
                    break;
            }
        }
    };

    protected abstract View createNewsView();

    protected abstract Object loadData();
}
