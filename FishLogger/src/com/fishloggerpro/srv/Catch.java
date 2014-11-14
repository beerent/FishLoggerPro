package com.fishloggerpro.srv;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class Catch implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String note;
	private String species;
	private String bait;
	private String conditions;
	private String latitude;
	private String longitude;
	private double weight;
	private double length;
	private Date date;
	private File image;

	/**
	 * Create a new Catch type. Image support to be added later
	 * 
	 * @param species
	 * @param note
	 * @param weight
	 * @param length
	 * @param bait
	 */
	public Catch(String species, String note, double weight, double length,
			String bait, String conditions, String latitude, String longitude, File f) {
		this.species = species;
		this.note = note;
		this.weight = weight;
		this.length = length;
		this.bait = bait;
		this.conditions = conditions;
		this.latitude = latitude;
		this.longitude = longitude;
		this.date = new Date();
		this.image = f;
		
		
	}

	/**
	 * Returns a string formatted to be sent to the server
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(species + " ");
		str.append(weight + " ");
		str.append(length + " ");
		str.append(bait + " ");
		str.append(date.toString() + " ");
		str.append(note + " ");
		str.append(conditions + " ");
		str.append(latitude + " ");
		str.append(longitude + " ");
		return str.toString();
	}

	public String getNote() {
		return note;
	}

	public String getSpecies() {
		return species;
	}

	public double getWeight() {
		return weight;
	}

	public double getLength() {
		return length;
	}

	public String getBait() {
		return bait;
	}
	
	public String getLatitude(){
		return latitude;
	}
	
	public String getLongitude(){
		return longitude;
	}
	
	public String getConditions(){
		return conditions;
	}

	public Date getDate() {
		return date;
	}
	
	public File getImage(){
		return image;
	}
}