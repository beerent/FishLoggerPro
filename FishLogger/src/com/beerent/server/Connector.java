package com.beerent.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;

public class Connector {
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private static ConnectionManager connectionManager;
	private DatabaseHandler databaseHandler;
	
	private String username;
	
	/*
	 * accepts a socket connected to a client, 
	 * as well as a reference to the static connectionManager
	 * 
	 * also establishes a printwriter to print to the client, as well as a bufferedreader
	 * to read in from the client
	 */
	public Connector(Socket socket, ConnectionManager connectionManager){
		this.socket = socket;
		this.connectionManager = connectionManager;
		this.databaseHandler = new DatabaseHandler();
		try {
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * attempts to read a line from the client. If successful, returns the String.
	 */
	private String readFromClient(){
		try {
			return in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * handles a new connection from the client
	 */
	public void newConnection(){
		// tell client we are ready for input
		out.println("OK");
		
		//breaks the request into an array, delimited by a ";" character
		String request = readFromClient();
		System.out.println("got: " + request);
		
		if (request.equals("new"))           addNewUser();
		else if (request.equals("login"))    validateCredentials();
		else{
			String user = connectionManager.getUserByConnectionKey(request);
			if(user != null){
				out.println("OK");
				request = readFromClient();
				if(request.equals("add")) insertStatement();
				if(request.equals("get")) selectStatement();
			}	
		}
		closeConnection();
	}
	
	private void addNewUser(){
		out.println("OK");
		String username = readFromClient();
		out.println("OK");
		String password = readFromClient();
		try {
			boolean result = databaseHandler.addUser(username, password);
			if (result){
				out.println("1");
			}else{
				out.println("-1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void validateCredentials(){
		out.println("OK");
		String username = readFromClient();
		out.println("OK");
		String password = readFromClient();
		
		this.username = username;
		try {
			boolean result = databaseHandler.validateUser(this.username, password);
			if (result){
				//generate and send key
				String key = connectionManager.addConnection(this.username);
				System.out.println("user: " + username + " authenticated");
				out.println(key);
			}else{
				System.out.println("user denied");
				//authentication denied
				out.println("-1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void insertStatement(){
		System.out.println("add statement");
		out.println("OK");
		try {
			ObjectInputStream inputStream = new ObjectInputStream(this.socket.getInputStream());
			System.out.println("input stream instantiated");
			Catch c = (Catch) inputStream.readObject();
			System.out.println("catch obj received");		
			System.out.println(c);
			out.println("OK!");
		} catch (Exception e) {
			System.out.println("fail catching Catch object!");
		}
	}
	
	private void selectStatement(){
		
	}
	
	//closes the socket connection with client
	private void closeConnection(){
		try {
			this.socket.close();
			connectionManager.closeConnection(this.username);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}