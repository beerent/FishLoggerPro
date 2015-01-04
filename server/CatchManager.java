import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class CatchManager {
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String username;
	private DatabaseHandler databaseHandler;
	
	public CatchManager(String username, Socket socket, PrintWriter out, BufferedReader in, DatabaseHandler databaseHandler){
		this.databaseHandler = databaseHandler;
		this.username = username;
		this.socket = socket;
		this.out = out;
		this.in = in;
	}
	
	public void addCatch(){
		System.out.println("add statement!");

		out.println("OK1");
		addCatchToDatabase();
	}
	
	private void addCatchToDatabase() {
		System.out.println("collecting data");
		String data = readFromClient();
		String del = "$";

    	String [] items = data.split(",");
    	List<String> container = Arrays.asList(items);
    	
		out.println("OK");


		System.out.println(container);
		long size = -1;
		String s = readFromClient();
		if (s.equals("null")){
			System.out.println("found null for size");
			return;
		}
		size = Long.parseLong(s);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(container.get(8)));
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		
		
		String nextCatch = databaseHandler.getNextCatchForUser(this.username);
		String directory = "/home/brent/Documents/images/";
		String imgLocation = this.username + "/" + year + "/" + (month+1) + "/" + nextCatch + ".jpg";
		
		out.println("OK");
		
		getAndSaveFile(directory + imgLocation, size);
		System.out.println("adding to DB");

		String lon = "-1";
		String lat = "-1";
		databaseHandler.insertCatch(this.username, container.get(0), container.get(1), container.get(2), container.get(3), lon, lat, container.get(6), container.get(7), container.get(8), imgLocation);
		this.out.println("OK");
		System.out.println("complete.");
	}
	
	private void getAndSaveFile(String imgLocation, long size){
		FileOutputStream out = null;
		File file;
		try {
			file = new File(imgLocation); 	
			if (!file.exists()) {
				file.createNewFile();
			}	
			java.io.InputStream in = socket.getInputStream();
			out = new FileOutputStream(new File (imgLocation));
			byte[] buf = new byte[9000];
			int len = 0;
			int total = 0;
			System.out.println("writing file");
			while (((len = in.read(buf)) !=-1)) {
				total += len;
				if (total == size)
					break;
				
				out.write(buf, 0, len);
				buf = new byte[9000];
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getCatch(){
		
	}
	
	/*
	 * attempts to read a line from the client. If successful, returns the
	 * String.
	 */
	private String readFromClient() {
		try {
			return in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}