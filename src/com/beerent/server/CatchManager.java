import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

import com.fishloggerpro.srv.Catch;

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
		System.out.println("add statement");
		out.println("OK");
		try {
			ObjectInputStream inputStream = new ObjectInputStream(
					this.socket.getInputStream());
			System.out.println("input stream instantiated");
			Catch c = (Catch) inputStream.readObject();
			System.out.println("caught catch obj");
			addCatchToDatabase(c);
		} catch (Exception e) {
			System.out.println("fail catching Catch object!");
			e.printStackTrace();
		}
	}
	
	private void addCatchToDatabase(Catch c) {
		System.out.println("collecting data");
		String note = c.getNote();
		String species = c.getSpecies();
		String bait = c.getBait();
		String conditions = c.getConditions();
		String latitude = c.getLatitude();
		String longitude = c.getLongitude();
		double weight = c.getWeight();
		double length = c.getLength();
		Date date = c.getDate();
		System.out.println("data collected");

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		
		String nextCatch = databaseHandler.getNextCatchForUser(this.username);
		String directory = "/home/brent/workspace/FishLogger/src/com/fishloggerpro/srv/images/";
		String imgLocation = this.username + "/" + year + "/" + (month+1) + "/" + nextCatch + ".jpg";
		
		out.println("OK");
		int size = Integer.parseInt(readFromClient());
		out.println("OK");
		
		getAndSaveFile(directory + imgLocation, size);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = sdf.format(date);
		System.out.println("adding to DB");
		databaseHandler.insertCatch(this.username, formattedDate, bait, note, conditions, species, length, weight, imgLocation);
		this.out.println("OK");
		System.out.println("complete.");
	}
	
	private void getAndSaveFile(String imgLocation, int size){
		FileOutputStream out = null;
		File file;
		try {
			file = new File(imgLocation);
			out = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}	
			java.io.InputStream in = socket.getInputStream();
			//OutputStream out = new FileOutputStream(new File (directory + imgLocation));
			byte[] buf = new byte[9000];
			int len = 0;
			System.out.println("writing file");
			while ((len += in.read(buf)) < size) {
				System.out.println(len);
				out.write(buf, 0, len);
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
