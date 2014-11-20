public class User {
	private String username;
	private int userID;
	
	public User(String username, int userID){
		this.username = username;
		this.userID = userID;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public int getUserID(){
		return this.userID;
	}
}
