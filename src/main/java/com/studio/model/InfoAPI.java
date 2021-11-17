package com.studio.model;

public class InfoAPI {
	String method, link;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public InfoAPI(String method, String link) {
		super();
		this.method = method;
		this.link = link;
	}
	

}
