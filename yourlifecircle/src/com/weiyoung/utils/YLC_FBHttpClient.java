package com.weiyoung.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.weiyoung.variable.LoginInfo;
import com.weiyoung.yourlifecircle.YLC_APPLICATION;

public class YLC_FBHttpClient {
	private String uri;
	public boolean saveFB(String fbprobolem){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
		uri="http://"+YLC_APPLICATION.ip+"/yourlifecircle/feedAdviceAction.action";
		NameValuePair nickname=new BasicNameValuePair("feedAdvice.feedUser",LoginInfo.nickname);
		NameValuePair data=new BasicNameValuePair("feedAdvice.dataTime",sdf.format(new java.util.Date()));
		NameValuePair problem=new BasicNameValuePair("feedAdvice.feedProblem",fbprobolem);
		List<NameValuePair> list=new ArrayList<NameValuePair>();
		list.add(nickname);
		list.add(data);
		list.add(problem);
		try {
			HttpEntity httpEntity=new UrlEncodedFormEntity(list,"utf-8");
			HttpPost httpPost=new HttpPost(uri);
			httpPost.setEntity(httpEntity);
			HttpClient httpClient=new DefaultHttpClient();
			HttpResponse response=httpClient.execute(httpPost);
			//处理返回值
			httpEntity=response.getEntity();
			InputStream is=httpEntity.getContent();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			String result="";
			String line="";
			while((line=br.readLine())!=null){
				result+=line;
			}
			System.out.println("result:"+result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public List<Map<String, String>> getFB(){
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		uri="http://"+YLC_APPLICATION.ip+"/yourlifecircle/selectedAllFeedService.action";
		HttpGet request=new HttpGet(uri);
		HttpClient httpClient=new DefaultHttpClient();
		HttpResponse response;
		try {
			response=httpClient.execute(request);
			HttpEntity httpEntity=response.getEntity();
			InputStream is=httpEntity.getContent();
			BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf-8"));
			String result="";
			String line="";
			while((line=br.readLine())!=null){
				result+=line;
			}
			System.out.println(result);
			JSONArray array=new JSONArray(result);
			for(int i=0;i<array.length();i++){
				JSONObject jsonObject=array.getJSONObject(i);
				Map<String, String> map=new HashMap<String, String>();
				map.put("textView_user_feeduser", jsonObject.getString("feedUser"));
				map.put("textView_user_feedtime", jsonObject.getString("dataTime"));
				map.put("textView_user_feedquestion", jsonObject.getString("feedProblem"));
				list.add(map);
			}
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
		
		
		
		
		
		return list;
	}
}
