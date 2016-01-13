package com.weiyoung.yourlifecircle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.weiyoung.utils.YLC_FBHttpClient;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class YLC_User_Feedback extends Activity {
	private Button button_user_feedback;
	private ListView listView_user_feedback;
	private List<Map<String, String>> listAdvice;
	private YLC_FBHttpClient ylc_fbhttpclient;
	private YLC_User_Feedback_Adapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc_user_feedback);
		initData();
		button_user_feedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(YLC_User_Feedback.this, YLC_User_SendFeedback.class);
				YLC_User_Feedback.this.startActivity(intent);
			}
		});
		//从网络获取数据,填充到listAdvice中，这里只初始化一下，并没有数据
		//listAdvice=getDataFromNetwork();
		ylc_fbhttpclient=new YLC_FBHttpClient();
		adapter=new YLC_User_Feedback_Adapter(YLC_User_Feedback.this);
		listAdvice=ylc_fbhttpclient.getFB();
		Log.i("TAG", ""+listAdvice.size());
		listView_user_feedback.setAdapter(adapter);
	}
	class YLC_User_Feedback_Adapter extends BaseAdapter{
		LayoutInflater layoutInflater;
		public YLC_User_Feedback_Adapter(Context context){
			layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listAdvice.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolderOfUserFeedback vh;
			if(convertView==null){
				vh=new ViewHolderOfUserFeedback();
				convertView=layoutInflater.inflate(R.layout.activity_ylc_user_feedback_item, parent, false);
				vh.textView_user_feeduser=(TextView) convertView.findViewById(R.id.textView_user_feeduser);
				vh.textView_user_feedtime=(TextView) convertView.findViewById(R.id.textView_user_feedtime);
				vh.textView_user_feedquestion=(TextView) convertView.findViewById(R.id.textView_user_feedquestion);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolderOfUserFeedback) convertView.getTag();
			}
			Map map=listAdvice.get(position);
			vh.textView_user_feeduser.setText("用户名："+map.get("textView_user_feeduser").toString());
			vh.textView_user_feedtime.setText("发表时间："+map.get("textView_user_feedtime").toString());
			vh.textView_user_feedquestion.setText("问题："+map.get("textView_user_feedquestion").toString());
			return convertView;
		}
		
	}
	class ViewHolderOfUserFeedback{
		TextView textView_user_feeduser;
		TextView textView_user_feedtime;
		TextView textView_user_feedquestion;
	}
	private void initData() {
		// TODO Auto-generated method stub
		button_user_feedback=(Button) findViewById(R.id.button_user_feedback);
		listView_user_feedback=(ListView) findViewById(R.id.listView_user_feedback);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__user__feedback, menu);
		return true;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		listAdvice=ylc_fbhttpclient.getFB();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		listAdvice=ylc_fbhttpclient.getFB();
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		listAdvice=ylc_fbhttpclient.getFB();
		adapter.notifyDataSetChanged();
	}
	
}
