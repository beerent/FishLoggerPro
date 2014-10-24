package com.beerent.client.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClient {
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private static String username;
	private static String password;
	private String connectionKey;
	
	
	public static void main(String[] args) {
		TestClient testClient = new TestClient("beerent", "password1");
		//testClient.testNewUser(username, password);
		testClient.testLogin(username, password);
		//testClient.testAddFish();
		
	}
	
	public TestClient(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	private void connect(){
		try {
			socket = new Socket("localhost", 5678);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testNewUser(String username, String password){
		connect();
		read(); //read OK
		out.println("new;"+username+";"+password);
		System.out.println(read());
	}
	
	public void testLogin(String username, String password){
		connect();
		read();
		out.println("login;"+username+";"+password);
		String result = read();
		this.connectionKey = result;
		System.out.println(result + " of length " + result.length());
	}
	
	public void testAddFish(){
		connect();
		read();
		out.println("add;"+this.connectionKey);
		if(read().equals("-1"))
			System.out.println("connection authentication failed");
		else{
			System.out.println("user recognized");
		}
	}
	
	public void testGetFish(PrintWriter out, BufferedReader in){
		
	}
	
	public String read(){
		try{
			return in.readLine();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return null;
	}
	
	private void write(String str){
		out.println(str);		
	}
}