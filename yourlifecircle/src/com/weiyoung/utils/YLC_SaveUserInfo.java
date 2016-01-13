package com.weiyoung.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.weiyoung.model.QQUser;
import com.weiyoung.model.QQWBUser;
import com.weiyoung.variable.LoginInfo;
import com.weiyoung.yourlifecircle.YLC_APPLICATION;

public class YLC_SaveUserInfo {
	private Context context;
	private JSONObject qqUserJson;
	private JSONObject qqWBUserJson;
	public YLC_SaveUserInfo(Context context){
		qqUserJson=YLC_GETQQINFO.getQQInfo("json");
		qqWBUserJson=YLC_GETQQWEIBOINFO.getQQWebInfo("json");
		this.context=context;
	}
	public boolean saveUser(){
		JSONObject jsonData=null;
		String uri="http://"+YLC_APPLICATION.ip+"/yourlifecircle/";
		List<NameValuePair> list=new ArrayList<NameValuePair>();
		try {
			jsonData=qqWBUserJson.getJSONObject("data");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(jsonData==null){
			//这个为空的话，则是qq用户
			LoginInfo.user_type=1;
			list.clear();
			QQUser qqUser=new QQUser();
			try {
				qqUser.setNickname(qqUserJson.getString("nickname"));
				qqUser.setGender(qqUserJson.getString("gender"));
				qqUser.setFigureurl_qq_1(qqUserJson.getString("figureurl_qq_1"));
				qqUser.setFigureurl_qq_2(qqUserJson.getString("figureurl_qq_2"));
				qqUser.setIs_yellow_vidp(Integer.parseInt(qqUserJson.getString("is_yellow_vip")));
				qqUser.setVip(Integer.parseInt(qqUserJson.getString("vip")));
				qqUser.setYellow_vip_level(Integer.parseInt(qqUserJson.getString("yellow_vip_level")));
				qqUser.setLevel(Integer.parseInt(qqUserJson.getString("level")));
				qqUser.setIs_yellow_year_vip(Integer.parseInt(qqUserJson.getString("is_yellow_year_vip")));
				qqUser.setUsertype(1);
				//存储数据到sharedpreferces
				SharedPreferences share=context.getSharedPreferences("qquser", Context.MODE_PRIVATE);
				Editor editor=share.edit();
				editor.putString("nickname", qqUser.getNickname());
				editor.putString("gender", qqUser.getGender());
				editor.putInt("is_yellow_vip", qqUser.getIs_yellow_vidp());
				editor.putInt("vip", qqUser.getVip());
				editor.putInt("yellow_vip_level",qqUser.getYellow_vip_level());
				editor.putInt("level", qqUser.getLevel());
				editor.putInt("is_yellow_year_vip", qqUser.getIs_yellow_year_vip());
				editor.commit();
				LoginInfo.userinfo_state=true;
				
				NameValuePair nickname=new BasicNameValuePair("qqUser.nickname", qqUser.getNickname());
				NameValuePair gender=new BasicNameValuePair("qqUser.gender", qqUser.getGender());
				NameValuePair figureurl_qq_1=new BasicNameValuePair("qqUser.figureurl_qq_1", qqUser.getFigureurl_qq_1());
				NameValuePair figureurl_qq_2=new BasicNameValuePair("qqUser.figureurl_qq_2", qqUser.getFigureurl_qq_2());
				NameValuePair is_yellow_vidp=new BasicNameValuePair("qqUser.is_yellow_vidp", String.valueOf(qqUser.getIs_yellow_vidp()));
				NameValuePair vip=new BasicNameValuePair("qqUser.vip", String.valueOf(qqUser.getVip()));
				NameValuePair yellow_vip_level=new BasicNameValuePair("qqUser.yellow_vip_level", String.valueOf(qqUser.getYellow_vip_level()));
				NameValuePair level=new BasicNameValuePair("qqUser.level", String.valueOf(qqUser.getLevel()));
				NameValuePair is_yellow_year_vip=new BasicNameValuePair("qqUser.is_yellow_year_vip", String.valueOf(qqUser.getIs_yellow_year_vip()));
				NameValuePair usertype=new BasicNameValuePair("qqUser.usertype", String.valueOf(qqUser.getUsertype()));
				list.add(nickname);
				list.add(gender);
				list.add(figureurl_qq_1);
				list.add(figureurl_qq_2);
				list.add(is_yellow_vidp);
				list.add(vip);
				list.add(yellow_vip_level);
				list.add(level);
				list.add(is_yellow_year_vip);
				list.add(usertype);
				HttpEntity httpEntity=new UrlEncodedFormEntity(list,"utf-8");
				HttpPost post=new HttpPost(uri+"qqUser/qqusersave.action");
				post.setEntity(httpEntity);
				HttpClient client=new DefaultHttpClient();
				HttpResponse response=client.execute(post);
				httpEntity=response.getEntity();
				InputStream is=httpEntity.getContent();
				BufferedReader br=new BufferedReader(new InputStreamReader(is));
				String result="";
				String line="";
				while((line=br.readLine())!=null){
					result+=line;
				}
				
				JSONObject json=new JSONObject(result);
				int i=json.getInt("result");
				if(i==1||i==2){
					return true;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("e3");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			//不为空的话，就是qq微博用户
			LoginInfo.user_type=2;
			QQWBUser qqWBUser=new QQWBUser();
			list.clear();
			try {
				qqWBUser.setNick(jsonData.getString("nick"));
				System.out.println(jsonData.getString("nick"));
				qqWBUser.setNickName(qqUserJson.getString("nickname"));
				System.out.println(qqUserJson.getString("nickname"));
				qqWBUser.setWbCount(jsonData.getString("name"));
				System.out.println(jsonData.getString("name"));
				qqWBUser.setGender(qqUserJson.getString("gender"));
				System.out.println(qqUserJson.getString("gender"));
				qqWBUser.setFigureurl_qq_1(qqUserJson.getString("figureurl_qq_1"));
				System.out.println(qqUserJson.getString("figureurl_qq_1"));
				qqWBUser.setFigureurl_qq_2(qqUserJson.getString("figureurl_qq_2"));
				System.out.println(qqUserJson.getString("figureurl_qq_2"));
				qqWBUser.setIs_yellow_vidp(Integer.parseInt(qqUserJson.getString("is_yellow_vip")));
				System.out.println(qqUserJson.getString("is_yellow_vip"));
				qqWBUser.setVip(Integer.parseInt(qqUserJson.getString("vip")));
				qqWBUser.setYellow_vip_level(Integer.parseInt(qqUserJson.getString("yellow_vip_level")));
				qqWBUser.setLevel(Integer.parseInt(qqUserJson.getString("level")));
				qqWBUser.setIs_yellow_year_vip(Integer.parseInt(qqUserJson.getString("is_yellow_year_vip")));
				qqWBUser.setBirth_year(jsonData.getString("birth_year"));
				qqWBUser.setBirth_month(jsonData.getString("birth_month"));
				qqWBUser.setBirth_day(jsonData.getString("birth_day"));
				qqWBUser.setLocation(jsonData.getString("location"));
				qqWBUser.setUsertype(2);
				//存储数据到sharedpreferces
				SharedPreferences share=context.getSharedPreferences("qqwbuser", Context.MODE_PRIVATE);
				Editor editor=share.edit();
				editor.putString("nick", qqWBUser.getNick());
				editor.putString("nickname", qqWBUser.getNickName());
				editor.putString("wbcount", qqWBUser.getWbCount());
				editor.putString("gender", qqWBUser.getGender());
				editor.putInt("is_yellow_vip", qqWBUser.getIs_yellow_vidp());
				editor.putInt("vip", qqWBUser.getVip());
				editor.putInt("yellow_vip_level",qqWBUser.getYellow_vip_level());
				editor.putInt("level", qqWBUser.getLevel());
				editor.putInt("is_yellow_year_vip", qqWBUser.getIs_yellow_year_vip());
				editor.putString("birthday", qqWBUser.getBirth_year()+"-"+qqWBUser.getBirth_month()+"-"+qqWBUser.getBirth_day());
				editor.putString("location", qqWBUser.getLocation());
				editor.commit();
				LoginInfo.userinfo_state=true;
				NameValuePair nick=new BasicNameValuePair("qqWBUser.nick", qqWBUser.getNick());
				NameValuePair nickName=new BasicNameValuePair("qqWBUser.nickName", qqWBUser.getNickName());
				NameValuePair wbCount=new BasicNameValuePair("qqWBUser.wbCount", qqWBUser.getGender());
				NameValuePair gender=new BasicNameValuePair("qqWBUser.gender", qqWBUser.getNick());
				NameValuePair figureurl_qq_1=new BasicNameValuePair("qqWBUser.figureurl_qq_1", qqWBUser.getFigureurl_qq_1());
				NameValuePair figureurl_qq_2=new BasicNameValuePair("qqWBUser.figureurl_qq_2", qqWBUser.getFigureurl_qq_2());
				NameValuePair is_yellow_vidp=new BasicNameValuePair("qqWBUser.is_yellow_vidp", String.valueOf(qqWBUser.getIs_yellow_vidp()));
				NameValuePair vip=new BasicNameValuePair("qqWBUser.vip",  String.valueOf(qqWBUser.getVip()));
				NameValuePair yellow_vip_level=new BasicNameValuePair("qqWBUser.yellow_vip_level", String.valueOf(qqWBUser.getYellow_vip_level()));
				NameValuePair level=new BasicNameValuePair("qqWBUser.level", String.valueOf(qqWBUser.getLevel()));
				NameValuePair is_yellow_year_vip=new BasicNameValuePair("qqWBUser.is_yellow_year_vip", String.valueOf(qqWBUser.getIs_yellow_year_vip()));
				NameValuePair birth_year=new BasicNameValuePair("qqWBUser.birth_year", qqWBUser.getBirth_year());
				NameValuePair birth_month=new BasicNameValuePair("qqWBUser.birth_month", qqWBUser.getBirth_month());
				NameValuePair birth_day=new BasicNameValuePair("qqWBUser.birth_day", qqWBUser.getBirth_day());
				NameValuePair location=new BasicNameValuePair("qqWBUser.location", qqWBUser.getLocation());
				NameValuePair usertype=new BasicNameValuePair("qqWBUser.usertype", String.valueOf(qqWBUser.getUsertype()));
				list.add(nick);
				list.add(nickName);
				list.add(wbCount);
				list.add(gender);
				list.add(figureurl_qq_1);
				list.add(figureurl_qq_2);
				list.add(is_yellow_vidp);
				list.add(yellow_vip_level);
				list.add(level);
				list.add(vip);
				list.add(is_yellow_year_vip);
				list.add(birth_year);
				list.add(birth_month);
				list.add(birth_day);
				list.add(location);
				list.add(usertype);
				HttpEntity httpEntity=new UrlEncodedFormEntity(list,"utf-8");
				HttpPost post=new HttpPost(uri+"qqWBUser/qqwbusersave.action");
				post.setEntity(httpEntity);
				HttpClient client=new DefaultHttpClient();
				HttpResponse response=client.execute(post);
				httpEntity=response.getEntity();
				InputStream is=httpEntity.getContent();
				BufferedReader br=new BufferedReader(new InputStreamReader(is));
				String result="";
				String line="";
				while((line=br.readLine())!=null){
					result+=line;
					
				}
				JSONObject json=new JSONObject(result);
				int i=json.getInt("result");
				if(i==1||i==2){
					return true;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("e4");
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
