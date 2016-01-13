package com.weiyoung.model;

public class QQWBUser {
	private String nick;			//腾讯微博用户昵称
	private String nickName;
	private String wbCount;			//name,在获取到的数据中是以name显示的
	private String gender;			//性别
	private String figureurl_qq_1;	//qq头像*40
	private String figureurl_qq_2;	//qq头像*100
	private int  is_yellow_vidp;
	private int vip;
	private int yellow_vip_level;
	private int level;
	private int is_yellow_year_vip;
	private String birth_year;
	private String birth_month;
	private String birth_day;
	private String location;
	private int usertype;			//用户类型，usertype=2表示微博用户。
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getWbCount() {
		return wbCount;
	}
	public void setWbCount(String wbCount) {
		this.wbCount = wbCount;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getFigureurl_qq_1() {
		return figureurl_qq_1;
	}
	public void setFigureurl_qq_1(String figureurl_qq_1) {
		this.figureurl_qq_1 = figureurl_qq_1;
	}
	public String getFigureurl_qq_2() {
		return figureurl_qq_2;
	}
	public void setFigureurl_qq_2(String figureurl_qq_2) {
		this.figureurl_qq_2 = figureurl_qq_2;
	}
	public int getIs_yellow_vidp() {
		return is_yellow_vidp;
	}
	public void setIs_yellow_vidp(int is_yellow_vidp) {
		this.is_yellow_vidp = is_yellow_vidp;
	}
	public int getVip() {
		return vip;
	}
	public void setVip(int vip) {
		this.vip = vip;
	}
	public int getYellow_vip_level() {
		return yellow_vip_level;
	}
	public void setYellow_vip_level(int yellow_vip_level) {
		this.yellow_vip_level = yellow_vip_level;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getIs_yellow_year_vip() {
		return is_yellow_year_vip;
	}
	public void setIs_yellow_year_vip(int is_yellow_year_vip) {
		this.is_yellow_year_vip = is_yellow_year_vip;
	}
	public String getBirth_year() {
		return birth_year;
	}
	public void setBirth_year(String birth_year) {
		this.birth_year = birth_year;
	}
	public String getBirth_month() {
		return birth_month;
	}
	public void setBirth_month(String birth_month) {
		this.birth_month = birth_month;
	}
	public String getBirth_day() {
		return birth_day;
	}
	public void setBirth_day(String birth_day) {
		this.birth_day = birth_day;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	
}
