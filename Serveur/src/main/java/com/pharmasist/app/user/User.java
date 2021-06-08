package com.pharmasist.app.user;

import java.sql.Time;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity /* Mean that this must be table in the database */
@Table(name="users") /* The name of this class in the database is 'users' */
public class User {
	
	/* Table varibles */
	
	@Id /* Id of table user */
	@GeneratedValue(strategy=GenerationType.AUTO) /* Id auto increment */ 
	private int id;
	private String fname;
	private String lname;
	@Column(unique = true) /* The value must be unique in the user table */
	private String phone;
	@Column(unique = true) /* The value must be unique in the user table */
	private String username;
	private String pass;
	private double locationx;
	private double locationy;
	private Time moorningStart;
	private Time moorningEnd;
	private Time eveningStart;
	private Time eveningEnd;
	
	/* Getters and setters */
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public double getLocationx() {
		return locationx;
	}
	public void setLocationx(double locationx) {
		this.locationx = locationx;
	}
	public double getLocationy() {
		return locationy;
	}
	public void setLocationy(double locationy) {
		this.locationy = locationy;
	}
	public Time getMoorningStart() {
		return moorningStart;
	}
	public void setMoorningStart(Time moorningStart) {
		this.moorningStart = moorningStart;
	}
	public Time getMoorningEnd() {
		return moorningEnd;
	}
	public void setMoorningEnd(Time moorningEnd) {
		this.moorningEnd = moorningEnd;
	}
	public Time getEveningStart() {
		return eveningStart;
	}
	public void setEveningStart(Time eveningStart) {
		this.eveningStart = eveningStart;
	}
	public Time getEveningEnd() {
		return eveningEnd;
	}
	public void setEveningEnd(Time eveningEnd) {
		this.eveningEnd = eveningEnd;
	}
	
	/* Constructor */
	
	public User(int id, String fname, String lname, String phone, String username, String pass, double locationx,
			double locationy, Time moorningStart, Time moorningEnd, Time eveningStart, Time eveningEnd) {
		super();
		this.id = id;
		this.fname = fname;
		this.lname = lname;
		this.phone = phone;
		this.username = username;
		this.pass = pass;
		this.locationx = locationx;
		this.locationy = locationy;
		this.moorningStart = moorningStart;
		this.moorningEnd = moorningEnd;
		this.eveningStart = eveningStart;
		this.eveningEnd = eveningEnd;
	}
	public User(String fname, String lname, String phone, String username, String pass, double locationx,
			double locationy, Time moorningStart, Time moorningEnd, Time eveningStart, Time eveningEnd) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.phone = phone;
		this.username = username;
		this.pass = pass;
		this.locationx = locationx;
		this.locationy = locationy;
		this.moorningStart = moorningStart;
		this.moorningEnd = moorningEnd;
		this.eveningStart = eveningStart;
		this.eveningEnd = eveningEnd;
	}
	public User(int id) {
		super();
		this.id = id;
	}
	public User() {
		super();
	}

}
