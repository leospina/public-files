package com.credibanco.ssh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

public class DBUtil {
	
	//private Environment dbconn ;
	private String dbURL="jdbc:oracle:thin:";
	
	public static void main(String[] args) {
		try {
			Environment env = EnvironmentReader.getInfoByAlias("DB","C:\\Data\\Assist\\2023\\Credibanco\\dist\\db.json");
        	
			new DBUtil(env).getLastFile("PRUEBAS_1");
		} catch ( SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public DBUtil(Environment dbconn) {
		super();
		//this.dbconn = dbconn;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.dbURL= dbURL+dbconn.getUser()+"/"+dbconn.getPasswd()+"@"+dbconn.getHost()+":"+dbconn.getPort()+":"+dbconn.getNode();
	}




	public String getLastFile(String alias) throws SQLException {
		String fileName = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(dbURL);
			if (conn != null) {
				System.out.println("Connected");
			}									//MTL.
			String sql = "SELECT FileName FROM MTL.TRACE_FILE WHERE Alias='" + alias	+ "' AND status='OK' ORDER BY DateProcess DESC";
			System.out.println(sql);
			preparedStatement = conn.prepareStatement(sql.toString());
			preparedStatement.execute();

			rs = preparedStatement.getResultSet();
			if (rs.next()) {
				fileName = rs.getString("FileName");
			}
			rs.close();
		} finally {
			if (rs != null)
				rs.close();
			if (preparedStatement != null)
				preparedStatement.close();
			if (conn != null)
				conn.close();
		}
		System.out.println("LastFile "+fileName);
		return fileName;

	}

	public int insertTrace(String alias,String file,String status) throws  SQLException {
		int resp=0;
		//Class.forName("oracle.jdbc.OracleDriver");
		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try {
			conn = DriverManager.getConnection(dbURL);
			if (conn != null) {
				System.out.println("Connected");
			}						 //MTL.	
			String sql = "INSERT INTO MTL.TRACE_FILE VALUES('" + alias	+ "' ,'" + file	+ "' ,'" + status	+ "',CURRENT_DATE)";
			preparedStatement = conn.prepareStatement(sql.toString());
			resp = preparedStatement.executeUpdate();


		} finally {

			if (preparedStatement != null)
				preparedStatement.close();
			if (conn != null)
				conn.close();
		}
		
		
		
		return resp;
		
	}
	public int updateTrace(String alias,String file,String status) throws  SQLException {
		int resp=0;
		//Class.forName("oracle.jdbc.OracleDriver");
		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try {
			conn = DriverManager.getConnection(dbURL);
			if (conn != null) {
				System.out.println("Connected");
			}                    //MTL.
			String sql = "UPDATE MTL.TRACE_FILE SET Status='" + status	+ "' WHERE FileName='" + file	+ "' AND Alias='" + alias	+ "'";
			preparedStatement = conn.prepareStatement(sql);
			resp = preparedStatement.executeUpdate();


		} finally {

			if (preparedStatement != null)
				preparedStatement.close();
			if (conn != null)
				conn.close();
		}

		return resp;
		
	}
	
	public  int insertData(HashMap<String, String> dataMap, String tableName) throws SQLException {

		int affectedRows = 0;
		//String dbURL = "jdbc:oracle:thin:system/oracle@localhost:49161:xe";
		//String dbURL = "jdbc:oracle:thin:CON_MTL/CoMtl_2023_du@172.27.1.237:1191:PRORA19";
		//Class.forName("oracle.jdbc.OracleDriver");
														//MTL.
		StringBuilder sql = new StringBuilder("INSERT INTO MTL.").append(tableName).append(" (");
		StringBuilder placeholders = new StringBuilder();

		for (Iterator<String> iter = dataMap.keySet().iterator(); iter.hasNext();) {
		    sql.append(iter.next());
		    placeholders.append("?");

		    if (iter.hasNext()) {
		        sql.append(",");
		        placeholders.append(",");
		    }
		}

		sql.append(") VALUES (").append(placeholders).append(")");
		System.out.println(sql);
		Connection conn =  null;
		PreparedStatement preparedStatement = null;
		try {
		 conn = DriverManager.getConnection(dbURL);
		if (conn != null) {
		    System.out.println("Connected");
		}
		preparedStatement = conn.prepareStatement(sql.toString());
		int i = 1;

		for (String value : dataMap.values()) {
		    preparedStatement.setObject(i++, value);
		}

		affectedRows = preparedStatement.executeUpdate();
		}finally {
			if(preparedStatement!=null)
				preparedStatement.close();
			if(conn != null)
				conn.close();
		}
		return affectedRows;
	}
}
