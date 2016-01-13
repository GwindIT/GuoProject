package com.weiyoung.variable;

public class LoginInfo {
	public static boolean LOGIN_STATE=false;		//检测是否登录
	public static int user_type=1;					//用户类别，默认是qq用户
	public static String nickname;					//获取用户名字
	public static String oauth_consumer_key="101027899";
	public static String openid;
	public static String format;
	public static String access_token;
	public static boolean userinfo_state=false;		//存在sharedpreference里面的数据是否存储完成
}
