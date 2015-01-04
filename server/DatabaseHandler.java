import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;

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
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.conn = DriverManager.getConnection(url+dbName,userName,password);
		} catch (Exception e) {
			e.printStackTrace();
		}		  
	}
	
	public String getNextCatchForUser(String username){
		stmt = createStatement();
		String sql = "select catchID from catch where username = '" + username + "' order by catchID desc limit 1";
		ResultSet result = execute(sql);
		if (result == null) System.out.println("sql == null");
		try {
			while(result.next()){
				return "" + (result.getInt("catchID")+1);
			}
			return "0";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "-1";
	}

	public void insertCatch(String username, String species, String note, String bait, String conditions, String latitude, String longitude, String weight, String length, String date, String imageLocation){
		Date d1 = null;
		try{
			SimpleDateFormat dateFormatOfStringInDB = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			d1 = dateFormatOfStringInDB.parse(date);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.format(d1);
		}catch(Exception e){

		}

		stmt = createStatement();	
		int nextCatch = Integer.parseInt(getNextCatchForUser(username));
		String sql = "insert into catch (catchID, username) values (" + nextCatch + ", '" + username + "')";
		execute(sql);
		System.out.println("1");
		
		stmt = createStatement();
		sql = "insert into fish_images (username, catchID, image_url) values ('" + username + "', " + nextCatch + " , '" + imageLocation + "')";
		execute(sql);
		System.out.println("2");
		
		stmt = createStatement();
		sql = "insert into catch_data (catchID, username, date, bait, length, note, conditions, species, weight, longitude, latitude) values " +
		"(" + nextCatch + ", '" + username + "', '" + date + "', '" + bait + "', " + length + ", '" + note + "', '" + conditions + "', '" + species + "', " + weight + ", " + longitude + ", " + latitude + ")";
		execute(sql);
		System.out.println("3");
	}
	
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
		System.out.println("tying: " + username + " with " + password); 
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