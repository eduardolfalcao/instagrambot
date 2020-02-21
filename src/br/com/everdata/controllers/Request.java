package br.com.everdata.controllers;

public class Request {

	private String login;
	private String password;
	private String postId;

	public Request(String login, String password, String postId) {
		super();
		this.login = login;
		this.password = password;
		this.postId = postId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPostId() {
		return postId;
	}
	
	public void setPostId(String postId) {
		this.postId = postId;
	}

}
