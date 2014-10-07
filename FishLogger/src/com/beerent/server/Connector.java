package com.beerent.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
	
	
	private void service(String[] request){
		if (request[0] == "add_fish") databaseHandler.addFish(username, in, out, Arrays.copyOfRange(request, 1, request.length));
		if (request[0] == "get_fish") databaseHandler.getFish(username, in, out, Arrays.copyOfRange(request, 1, request.length));
	}
	
	/*
	 * handles a new connection from the client
	 */
	public void newConnection(){
		// tell client we are ready for input
		out.println("OK");
		
		//breaks the request into an array, delimited by a ";" character
		String[] request = readFromClient().split("[;]");
		
		//if the first String == connectionKey, the rest of the [] is a request, so we pass it to service()
		String user = connectionManager.getConnectionKey(request[0]);
		if(user != null){
			this.username = user;
			service(Arrays.copyOfRange(request, 1, request.length));
		}
			
		// if the first String == username, then the second String 2 == password, lets authenticate, and add
		// the user as an active user.
		else if (validateCredentials(request[0], request[1])){
			connectionManager.addConnection(request[0]);
			out.println(connectionManager.getConnectionKey(request[0]));
		}else
			//invalid request or denied attempt, close connection.
			out.println("BYE");
	}
	
	// returns true if the user passed in valid password
	private boolean validateCredentials(String username, String password){
		this.username = username;
		return true;
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


