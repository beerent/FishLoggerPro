package com.beerent.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.*;

public class DatabaseHandler {
	private Connection conn;
	
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
	
	public void addFish(String username, BufferedReader in, PrintWriter out, String [] info){
		
	}
	
	public void getFish(String username, BufferedReader in, PrintWriter out, String [] info){
		
	}
	
	
}
