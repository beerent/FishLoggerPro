package com.beerent.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FishLoggerServer {
	private ServerSocket serverSocket;
	public ConnectionManager connectionManager;
	
	public static void main(String [] args){
		FishLoggerServer fishLoggerServer = new FishLoggerServer(5678);
		fishLoggerServer.runServer();
	}
	
	/* this method runs for the life of the program.
	 * 
	 * it simply listens for a new connection, establishes a socket,
	 * and passes the socket and a reference to connectionManager to 
	 * the Connector class.
	 */
	public void runServer(){
		Socket socket;
		try {
			while (true){
				System.out.println("listening for connection.");
				socket = this.serverSocket.accept();
				new Connector(socket, connectionManager).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FishLoggerServer(int port) {
		if (serverSocket == null){
			try{
				serverSocket = new ServerSocket(port);
				connectionManager = new ConnectionManager();
				//connectionManager.start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}