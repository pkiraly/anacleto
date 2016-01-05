package com.anacleto.view;


public class UserBean {
	private String user;
	
	private String realName;
	
	private String password;
	
	private boolean enabled = true;
	
	private boolean admin;
	
	private String  email;
	
	private boolean ipFilterEnabled = true;
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isIpFilterEnabled() {
		return ipFilterEnabled;
	}

	public void setIpFilterEnabled(boolean ipFilterEnabled) {
		this.ipFilterEnabled = ipFilterEnabled;
	}
	
	
	
}
