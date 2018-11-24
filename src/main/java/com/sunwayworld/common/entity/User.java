package com.sunwayworld.common.entity;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 5881162021893910353L;

	public User() {
	}

//	@Expose
	@SerializedName("ID")
	private String id;
	@Expose
	@SerializedName("USERCODE")
	private String usrnam;//用户名
	@Expose
	@SerializedName("PWD")
	private String pwd;//密码
	@Expose
	@SerializedName("USERNAME")
	private String fullname;//用户名称
	@Expose
	@SerializedName("TYPE")
	private String type="";

	@Expose
	@SerializedName("ADD_DATE")
	private String ADD_DATE;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String url="";//保存图片的路径

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getADD_DATE() {
		return ADD_DATE;
	}

	public void setADD_DATE(String ADD_DATE) {
		this.ADD_DATE = ADD_DATE;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsrnam() {
		return usrnam;
	}

	public void setUsrnam(String usrnam) {
		this.usrnam = usrnam;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

}
