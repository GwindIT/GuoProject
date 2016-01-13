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

/**
 * 
 * @author renjie
 *返回的应该是JSON格式的数据字符串
 */
public class YLC_GETQQWEIBOINFO {
	public static JSONObject getQQWebInfo(String format) {
		String url = "https://graph.qq.com/user/get_info?"
				+ "oauth_consumer_key=" + LoginInfo.oauth_consumer_key
				+ "&access_token=" + LoginInfo.access_token + "&openid="
				+ LoginInfo.openid + "&format="+LoginInfo.format;
		JSONObject json = null;
		HttpGet request=new HttpGet(url);
		HttpClient client=new DefaultHttpClient();
		HttpResponse response;
		try {
			response=client.execute(request);
			HttpEntity httpEntity=response.getEntity();
			InputStream is=httpEntity.getContent();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			String result="";
			String line;
			while((line=br.readLine())!=null){
				result+=line;
			}
			json=new JSONObject(result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("e2");
			e.printStackTrace();
		}
		return json;
	}
}
