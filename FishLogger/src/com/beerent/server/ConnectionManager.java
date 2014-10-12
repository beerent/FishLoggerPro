package com.beerent.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConnectionManager {
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
		for(int i = 0; i < 9; i++){
			key += (char) rand.nextInt(126);
		}
		
		//if the key happens to already exist... try again.
		if (connections.containsValue(key))
			return generateKey();			
		return key;
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
