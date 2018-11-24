package com.sunwayworld.common.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class SysConfig implements Serializable{

	public SysConfig() {
	}

	private int id;
	private String title;
	@SerializedName("logo")
	private String logoUri;
	@SerializedName("sysitem")
	private String featureList;
	private boolean activation;
	private Date expirydate;
	private boolean autoLogin;
	private boolean remeberPwd;
	private String autoLoginPwd;
	private String lastLoginUsrNam;
	private String syscode;
	private boolean autoLoginexpirydate;
	@SerializedName("mdmurl")
	private String baseurl;
	private boolean isUseNew;

	private String deskey;
	private boolean olNoticeConfig;
	private boolean isEncrypt;
	private boolean isSong;
	private boolean isShake;
	private boolean isLocation;
	private boolean uploadVideoFile;

	private int outLineInterval;
	private int onLineInterval;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLogoUri() {
		return logoUri;
	}

	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}

	public String getFeatureList() {
		return featureList;
	}

	public void setFeatureList(String featureList) {
		this.featureList = featureList;
	}

	public boolean isActivation() {
		return activation;
	}

	public void setActivation(boolean activation) {
		this.activation = activation;
	}

	public Date getExpirydate() {
		return expirydate;
	}

	public void setExpirydate(Date expirydate) {
		this.expirydate = expirydate;
	}

	public boolean isAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	public boolean isRemeberPwd() {
		return remeberPwd;
	}

	public void setRemeberPwd(boolean remeberPwd) {
		this.remeberPwd = remeberPwd;
	}

	public String getAutoLoginPwd() {
		return autoLoginPwd;
	}

	public void setAutoLoginPwd(String autoLoginPwd) {
		this.autoLoginPwd = autoLoginPwd;
	}

	public String getLastLoginUsrNam() {
		return lastLoginUsrNam;
	}

	public void setLastLoginUsrNam(String lastLoginUsrNam) {
		this.lastLoginUsrNam = lastLoginUsrNam;
	}

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public boolean isAutoLoginexpirydate() {
		return autoLoginexpirydate;
	}

	public void setAutoLoginexpirydate(boolean autoLoginexpirydate) {
		this.autoLoginexpirydate = autoLoginexpirydate;
	}

	public String getBaseurl() {
		return baseurl;
	}

	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}

	public boolean isUseNew() {
		return isUseNew;
	}

	public void setUseNew(boolean useNew) {
		isUseNew = useNew;
	}

	public String getDeskey() {
		return deskey;
	}

	public void setDeskey(String deskey) {
		this.deskey = deskey;
	}

	public boolean isOlNoticeConfig() {
		return olNoticeConfig;
	}

	public void setOlNoticeConfig(boolean olNoticeConfig) {
		this.olNoticeConfig = olNoticeConfig;
	}

	public boolean isEncrypt() {
		return isEncrypt;
	}

	public void setEncrypt(boolean encrypt) {
		isEncrypt = encrypt;
	}

	public boolean isSong() {
		return isSong;
	}

	public void setSong(boolean song) {
		isSong = song;
	}

	public boolean isShake() {
		return isShake;
	}

	public void setShake(boolean shake) {
		isShake = shake;
	}

	public boolean isLocation() {
		return isLocation;
	}

	public void setLocation(boolean location) {
		isLocation = location;
	}

	public boolean isUploadVideoFile() {
		return uploadVideoFile;
	}

	public void setUploadVideoFile(boolean uploadVideoFile) {
		this.uploadVideoFile = uploadVideoFile;
	}

	public int getOutLineInterval() {
		return outLineInterval;
	}

	public void setOutLineInterval(int outLineInterval) {
		this.outLineInterval = outLineInterval;
	}

	public int getOnLineInterval() {
		return onLineInterval;
	}

	public void setOnLineInterval(int onLineInterval) {
		this.onLineInterval = onLineInterval;
	}
}
