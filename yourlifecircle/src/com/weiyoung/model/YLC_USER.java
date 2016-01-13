package com.weiyoung.model;

public class YLC_USER {
	private int ylc_user_id;					// int(11) NOT NULL AUTO_INCREMENT,
	private String ylc_user_name;				// varchar(50) DEFAULT NULL,
	private String ylc_user_password;			// varchar(8) DEFAULT NULL,
	private String ylc_user_sex;				// char(2) DEFAULT NULL,
	private int ylc_user_age;					// int(11) DEFAULT NULL,
	private String ylc_user_friends;			// varchar(2000) DEFAULT NULL,
	private String ylc_user_locations;			// varchar(5000) DEFAULT NULL,
	private int ylc_user_timer;					// int(11) DEFAULT NULL,
	public int getYlc_user_id() {
		return ylc_user_id;
	}
	public void setYlc_user_id(int ylc_user_id) {
		this.ylc_user_id = ylc_user_id;
	}
	public String getYlc_user_name() {
		return ylc_user_name;
	}
	public void setYlc_user_name(String ylc_user_name) {
		this.ylc_user_name = ylc_user_name;
	}
	public String getYlc_user_password() {
		return ylc_user_password;
	}
	public void setYlc_user_password(String ylc_user_password) {
		this.ylc_user_password = ylc_user_password;
	}
	public String getYlc_user_sex() {
		return ylc_user_sex;
	}
	public void setYlc_user_sex(String ylc_user_sex) {
		this.ylc_user_sex = ylc_user_sex;
	}
	public int getYlc_user_age() {
		return ylc_user_age;
	}
	public void setYlc_user_age(int ylc_user_age) {
		this.ylc_user_age = ylc_user_age;
	}
	public String getYlc_user_friends() {
		return ylc_user_friends;
	}
	public void setYlc_user_friends(String ylc_user_friends) {
		this.ylc_user_friends = ylc_user_friends;
	}
	public String getYlc_user_locations() {
		return ylc_user_locations;
	}
	public void setYlc_user_locations(String ylc_user_locations) {
		this.ylc_user_locations = ylc_user_locations;
	}
	public int getYlc_user_timer() {
		return ylc_user_timer;
	}
	public void setYlc_user_timer(int ylc_user_timer) {
		this.ylc_user_timer = ylc_user_timer;
	}
	
}
