package com.weiyoung.service;

import com.weiyoung.utils.YLC_SaveUserInfo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class YLC_SAVEUSERSERVICE extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		System.out.println("---save service start--------");
		YLC_SaveUserInfo saveUserInfo=new YLC_SaveUserInfo(YLC_SAVEUSERSERVICE.this);
		saveUserInfo.saveUser();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
}
