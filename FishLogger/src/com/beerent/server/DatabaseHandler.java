package com.beerent.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.*;

public class DatabaseHandler {
	private Connection conn;
	private Statement stmt;
	
	public DatabaseHandler(){
		establishDBConnection();
	}
	
	public static void main(String [] args){
		DatabaseHandler dh = new DatabaseHandler();
	}
	
	private void establishDBConnection(){
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "fishlogger";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root"; 
		String password = "cuse1234";
		try {
			Class.forName(driver).newInstance();
			this.conn = DriverManager.getConnection(url+dbName,userName,password);
		} catch (Exception e) {
			e.printStackTrace();
		}		  
	}
	
	//public String selectStatement(String [] statement){
		
	//}
	
	//public String insertStatement(String [] statement){
		
	//}
	
	public boolean addUser(String username, String password) throws SQLException{
		stmt = createStatement();
		String sql = "select count(*) as total from users where username = \"" + username +"\"";
		ResultSet result = execute(sql);
		
		if(!result.next())
			return false;
		
		int count = result.getInt("total");
		if (count > 0)
			return false;
		
		stmt = createStatement();
		sql = String.format("insert into users (userID, username, password) values (%d, \"%s\", \"%s\")", countUsers(), username, password);
		execute(sql);
		return true;
	}	
	
	public boolean validateUser(String username, String password) throws SQLException{
		stmt = createStatement();
		String sql = "select count(*) as total from users where username = \"" + username +"\" and password = \"" + password + "\"";
		ResultSet result = execute(sql);
		
		if(!result.next())
			return false;
		
		return result.getInt("total") > 0;
	}
	
	
	private int countUsers(){
		stmt = createStatement();
		String sql = "select count(*) as total from users";
		ResultSet result = execute(sql);
		try {
			if(!result.next())
				return -1;
			return result.getInt("total");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private ResultSet execute(String sql){
		try{
			if(sql.contains("select"))
				return stmt.executeQuery(sql);
			stmt.execute(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private Statement createStatement(){
		try{
			return conn.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}