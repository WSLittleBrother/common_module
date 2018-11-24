package com.sunwayworld.common.dao.model;

import java.io.Serializable;

public class UpdateInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5209193229653518605L;
	private String message;
	private int id;
	private String name;
	private String type;
	private String updateurl;
	private int version;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUpdateurl() {
		return updateurl;
	}

	public void setUpdateurl(String updateurl) {
		this.updateurl = updateurl;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
