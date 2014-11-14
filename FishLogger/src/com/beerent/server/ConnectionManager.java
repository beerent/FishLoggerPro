package com.beerent.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConnectionManager extends Thread{
	private Map<String, String> connections; // Key: username, Value: connectionString
	private Map<String, Long> timer;         // Key: username, Value: last time they connected
	
	/* establishes a connections hashmap, as well as a timer hashmap */
	public ConnectionManager(){
		connections = new HashMap<String, String>();
		timer = new HashMap<String, Long>();
	}
	
	// Method returns a unique connection String of length ten.
	private String generateKey(){
		Random rand = new Random();
		String key = "";
		for(int i = 0; i <= 15; i++){
			key += (char) (rand.nextInt(90)+ 36);
		}
		
		//if the key happens to already exist... try again.
		if (connections.containsValue(key))
			return generateKey();			
		return key;
	}
	
	//Thread strictly detects if a user hasn't made a connection in an hour.
	//if they have not connected in an hour, log them out.
	public void run(){
		try {
			while(true){
				this.sleep(60 * 1000);
				for(String user: timer.keySet()){
					//if user hasn't connected for an hour, log them out
					Double t = ((System.nanoTime() - timer.get(user)) / 1000000000.0);
					if (t > 3600.0){
						closeConnection(user);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// adds a user to both maps; connections and timer
	public String addConnection(String user){
		String key = generateKey();
		this.connections.put(user, key);
		this.timer.put(getConnectionKey(user), System.nanoTime());
		return key;
	}
	
	//returns the connectionKey for a given user
	protected String getConnectionKey(String user){
		return connections.get(user);
	}
	
	//returns true of the 
	public String getUserByConnectionKey(String connectionKey){
		System.out.println("trying to get user...");
		for(String user: connections.keySet()){
			System.out.println("trying user: " + user);
			if (connections.get(user).equals(connectionKey))
				return user;
		}
		return null;
	}
	
	//removes the user from the connection maps
	public void closeConnection(String user){
		connections.remove(user);
		timer.remove(user);
	}
	
	// returns true if the user's value is the same as the passed in key
	public boolean validateConnection(String user, String value){
		return connections.containsKey(user) && connections.get(user).equals(value);
	}
}
