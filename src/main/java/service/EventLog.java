package service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import response.Events;
import util.KeyGenerator;

@Service
public class EventLog {
	
	private final KeyGenerator keyGen = new KeyGenerator();
	
	private final String password = "Password";
	private long loginTime;
	private boolean logedIn = false;
	private String accessKey = keyGen.generateKey(); 
	
	ArrayList<Long> logTimes = new ArrayList<Long>();
	ArrayList<String> eventLog = new ArrayList<String>();
	
	public void addEvent(String s) {
		logTimes.add(System.currentTimeMillis());
		eventLog.add(s); 
	}
	
	public Events getEventLog() {
		return new Events(logTimes, eventLog); 
	}
	
	public boolean attemptLogin(String password) {
		if(!this.password.equals(password)) return false;
		accessKey = keyGen.generateKey();
		logedIn = true;
		loginTime = System.currentTimeMillis();
		return true;
	}
	
	public void logout() {
		logedIn = false;
		accessKey = "";
	}
	
	public String getKey() {
		return accessKey;
	}
	
	public boolean isLogedIn() {
		if(logedIn) 
			if(System.currentTimeMillis() - loginTime > 600000) logout();//expire login after 10 minutes
		return logedIn;
	}

}
