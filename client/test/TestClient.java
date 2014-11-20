import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
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
		testClient.testAddFish();
		
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
		out.println("login");
		read();
		out.println(this.username);
		read();
		out.println(this.password);
		String result = read();
		if(result.equals("-1")){
			System.out.println("connection with server failed.");
			System.exit(1);
		}
		this.connectionKey = result;
		System.out.println("key: " + result + " of length " + result.length());
	}
	
	public void testAddFish(){
		connect();
		read();
		out.println(this.connectionKey);
		read();
		out.println("add");
		read();
		try {
			//String species, String note, double weight, double length, String bait, String conditions, String latitude, String longitude
			File f = new File("/home/brent/workspace/FishLogger/src/com/beerent/client/test/img.jpg");
			Catch c = new Catch("LM Bass", "this is the note", 12.6, 12.5, "crankbait", "rainy", "latitude", "longitude", f);
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			output.writeObject(c);
			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
