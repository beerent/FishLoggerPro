package com.beerent.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class DirectoryManager {
	private String root;
	
	public DirectoryManager(String root){
		this.root = root;		
	}
	
	private void createDirectory(String dir){
		new File(dir).mkdirs();		
	}
	
	private boolean directoryExists(File f){
		return f.exists() && f.isDirectory();
	}
	
	public String addFileToDirectory(String username, BufferedImage image, int year, int month, String destFileName){
		String directory = root + username + "/" + year + "/" + (month+1) + "/";
		System.out.println("today's folder: " + directory);
		if(!directoryExists(new File(directory))){
			createDirectory(directory);
		}	
		File dest = new File(directory);
		try {
			ImageIO.write(image, "jpg", dest);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return username + "/" + year + "/" + (month+1) + "/" + destFileName;
	}
}
