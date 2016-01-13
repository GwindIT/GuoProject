package com.weiyoung.yourlifecircle;

import com.weiyoung.utils.YLC_FBHttpClient;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class YLC_User_SendFeedback extends Activity {
	private Button button_user_sendfeedback;
	private EditText editText_user_sendfeedback;
	private YLC_FBHttpClient ylc_fbhttpclient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc_user_send_feedback);
		initData();
	}

	public void initData() {
		// TODO Auto-generated method stub
		button_user_sendfeedback=(Button) findViewById(R.id.button_user_sendfeedback);
		editText_user_sendfeedback=(EditText) findViewById(R.id.editText_user_sendfeedback);
		ylc_fbhttpclient=new YLC_FBHttpClient();
		button_user_sendfeedback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str=editText_user_sendfeedback.getText().toString();
				if(str.equals("")){
					Toast.makeText(YLC_User_SendFeedback.this, "输入不能为空，请输入内容！", Toast.LENGTH_LONG).show();
				}else{
					//将数据提交到服务器
					ylc_fbhttpclient.saveFB(str);
					Toast.makeText(YLC_User_SendFeedback.this, "意见提交成功", Toast.LENGTH_LONG).show();
					editText_user_sendfeedback.setText("");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__user__send_feedback, menu);
		return true;
	}

}
