package com.beerent.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		String[] request = readFromClient().split("[;]");
		String option = request[0];
		request = Arrays.copyOfRange(request, 1, request.length);
		
		if (option.equals("new"))           addNewUser(request);
		else if (option.equals("login"))    validateCredentials(request);
		else if(option.equals("add"))       addFish(request);
		else if(option.equals("get"))       getFish();
		closeConnection();
	}
	
	private void addNewUser(String[] information){
		try {
			boolean result = databaseHandler.addUser(information[0], information[1]);
			if (result){
				out.println("1");
			}else{
				out.println("-1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void validateCredentials(String[] credentials){
		this.username = credentials[0];
		String password = credentials[1];
		try {
			boolean result = databaseHandler.validateUser(this.username, password);
			if (result){
				//generate and send key
				String key = connectionManager.addConnection(this.username);
				out.println(key);
			}else{
				//authentication denied
				out.println("-1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void addFish(String [] request){
		//get user via connection string
		System.out.println("got client: " + request[0]);
		String user = connectionManager.getUserByConnectionKey(request[0]);
		if (user == null)
			out.println("-1");
		else{
			this.username = user;
			out.println("1");
		}
	}
	
	private void getFish(){
		
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