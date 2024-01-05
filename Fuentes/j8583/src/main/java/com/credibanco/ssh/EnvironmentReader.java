package com.credibanco.ssh;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EnvironmentReader {

	public static void main(String[] args) {
		
		try {
			JSONParser parser = new JSONParser();
			Reader reader = new FileReader("C:\\Data\\Assist\\2023\\Credibanco\\dist\\environment.json");

			Object jsonObj = parser.parse(reader);

			JSONArray jsonObject = (JSONArray) jsonObj;
			
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> it = jsonObject.iterator();
			while (it.hasNext()) {
				
				JSONObject env = it.next();
				String name = (String)env.get("alias");
				String node = (String)env.get("node");
				String host = (String)env.get("host");
				String port = (String)env.get("port");
				String user = (String)env.get("user");
				String passwd = (String)env.get("passwd");
				System.out.println("Name = " + name);
				new Environment(name, node, host, port, user, passwd);
			}

			
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static Environment getInfoByAlias(String alias,String path) {
		Environment resp=null;
		try {
			JSONParser parser = new JSONParser();
			Reader reader = new FileReader(path);

			Object jsonObj = parser.parse(reader);

			JSONArray jsonObject = (JSONArray) jsonObj;
			
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> it = jsonObject.iterator();
			while (it.hasNext()) {
				
				JSONObject env = it.next();
				String name = (String)env.get("alias");
				String node = (String)env.get("node");
				String host = (String)env.get("host");
				String port = (String)env.get("port");
				String user = (String)env.get("user");
				String passwd = (String)env.get("passwd");
				
				if(name.equalsIgnoreCase(alias)) {
					System.out.println("Name = " + name);
					resp = new Environment(name, node, host, port, user, passwd);
				}
				
			}

			
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
		
	}
}
