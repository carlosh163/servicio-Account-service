package com.springboot.appbanco.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

public class Client extends Person{

	//private String idClient;

	
	private String clientType; // Personal o Empresarial.
	
	
	
	private char state;
	
	public Client() {
		// TODO Auto-generated constructor stub
	}

	

	/*public String getIdClient() {
		return idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}*/
	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}


	public char getState() {
		return state;
	}

	public void setState(char state) {
		this.state = state;
	}


	

	



	
	

	


	
	
}
