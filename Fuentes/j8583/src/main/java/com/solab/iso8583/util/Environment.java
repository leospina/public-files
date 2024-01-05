package com.solab.iso8583.util;

import java.util.Base64;

public class Environment {

	private String name ;
	private String node ;
	private String host ;
	private String port ;
	private String user ;
	private String passwd ;
	
	public Environment(String name, String node, String host, String port, String user, String passwd) {
		super();
		this.name = name;
		this.node = node;
		this.host = host;
		this.port = port;
		this.user = user;
		this.passwd = passwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPasswd() {
		byte[] decodedBytes = Base64.getDecoder().decode(passwd);
		String decodedString = new String(decodedBytes);
		
		return decodedString;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	
	
	
}
