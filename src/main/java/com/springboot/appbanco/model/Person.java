package com.springboot.appbanco.model;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class Person {

	//private String codPersona;
	
	
	private String fullName;
	private String gender; // genero
	
	
	//@JsonSerialize(using = ToStringSerializer.class)
	@JsonFormat(pattern = "dd-MM-yyyy",shape = Shape.STRING)
	private Date dateOfBirth;
	//private Date dateOfBirth;
	
	
	private String documentType; //DNI o Carnet de Extrangeria.
	private String documentNumber; 
	

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	
	
	
	
}
