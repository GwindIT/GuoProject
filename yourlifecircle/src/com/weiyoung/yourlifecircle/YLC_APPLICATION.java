package com.weiyoung.yourlifecircle;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class YLC_APPLICATION extends Application {
	private static YLC_APPLICATION instance=null;
	BMapManager bMapManager=null;
	public static final String ip="10.120.11.253:8080";
	public static final String STR_KEY="mTz6OcEZL1vsVubcfUMqCUiM";
	public boolean m_bKeyRight = true;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance=this;
		initBMapManager(this);
	}
	public void initBMapManager(Context context) {
		// TODO Auto-generated method stub
		if(bMapManager==null){
			bMapManager=new BMapManager(context);
		}
		if(!bMapManager.init(STR_KEY, new MyMKGeneralListener())){
			Toast.makeText(YLC_APPLICATION.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}
	public static YLC_APPLICATION getInstance(){
		return instance;
	}
	static class MyMKGeneralListener implements MKGeneralListener{

		@Override
		public void onGetNetworkState(int isError) {
			// TODO Auto-generated method stub
			if(isError==MKEvent.ERROR_NETWORK_CONNECT){
				 Toast.makeText(YLC_APPLICATION.getInstance().getApplicationContext(), "您的网络出错啦！",
		                    Toast.LENGTH_LONG).show();
			}else if (isError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(YLC_APPLICATION.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
		}

		@Override
		public void onGetPermissionState(int isError) {
			// TODO Auto-generated method stub
			//非零值表示key验证未通过
            if (isError != 0) {
                //授权Key错误：
                Toast.makeText(YLC_APPLICATION.getInstance().getApplicationContext(), 
                        "请在 YLC_APPLICATION.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "+isError, Toast.LENGTH_LONG).show();
                YLC_APPLICATION.getInstance().m_bKeyRight = false;
            }
            else{
            	YLC_APPLICATION.getInstance().m_bKeyRight = true;
            	Toast.makeText(YLC_APPLICATION.getInstance().getApplicationContext(), 
                        "key认证成功", Toast.LENGTH_LONG).show();
            }
		}
		
	}
}
