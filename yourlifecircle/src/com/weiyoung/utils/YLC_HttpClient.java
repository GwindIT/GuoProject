package com.weiyoung.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.weiyoung.variable.LoginInfo;

import android.net.Uri;
import android.util.Log;

public class YLC_HttpClient {
	public  Uri getQQuserInfo(JSONObject json){
		Log.i("TAG", "start");
		System.out.println(json.toString());
		Uri uri=null;
		String url="https://graph.qq.com/user/get_simple_userinfo?";
		try {
			LoginInfo.openid=json.getString("openid").toString();
			LoginInfo.access_token=json.getString("access_token").toString();
			LoginInfo.format="json";
			url+="oauth_consumer_key="+LoginInfo.oauth_consumer_key+"&access_token="+LoginInfo.access_token+"&openid="+LoginInfo.openid+"&format="+LoginInfo.format;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*//启动一个线程，向服务器存储用户信息
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("-----------start thread save user---------");
				YLC_SaveUserInfo saveUserInfo=new YLC_SaveUserInfo();
				saveUserInfo.saveUser();
			}
		});
		thread.start();*/
		HttpGet request=new HttpGet(url);
		HttpClient httpClient=new DefaultHttpClient();
		HttpResponse response;
		try {
			response=httpClient.execute(request);
			JSONObject json1=dealHttpResponse(response);
			String str=json1.getString("figureurl_qq_2");
			LoginInfo.nickname=json1.getString("nickname");
//			str.replace("\\", "");
			uri=Uri.parse(str);
			Log.i("TAG", str);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("TAG", "end");
		return uri;
	}
	public static JSONObject dealHttpResponse(HttpResponse response){
		JSONObject json=null;
		if(null==response){
			return null;
		}
		HttpEntity httpEntity=response.getEntity();
		try {
			InputStream is=httpEntity.getContent();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			String result="";
			String line="";
			while((line=br.readLine())!=null){
				result+=line;
			}
			json=new JSONObject(result);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
		
}
