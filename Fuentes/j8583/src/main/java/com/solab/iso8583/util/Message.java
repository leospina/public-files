package com.solab.iso8583.util;

import java.util.HashMap;

public class Message {
	
	private String code;
	
	HashMap<String,String> campos;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public HashMap<String, String> getCampos() {
		return campos;
	}

	public void setCampos(HashMap<String, String> campos) {
		this.campos = campos;
	}
	
	

}
