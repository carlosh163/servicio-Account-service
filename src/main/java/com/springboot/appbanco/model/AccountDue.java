package com.springboot.appbanco.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class AccountDue {
	private String documentNumber;
	private Integer numberAccount; 
	private double dueAmount;
	
	@JsonFormat(pattern = "dd-MM-yyyy",shape = Shape.STRING)
	private Date dueDate;	
	private String bankName;

	
	public AccountDue() {
		// TODO Auto-generated constructor stub
	}


	public Integer getNumberAccount() {
		return numberAccount;
	}


	public void setNumberAccount(Integer numberAccount) {
		this.numberAccount = numberAccount;
	}


	public double getDueAmount() {
		return dueAmount;
	}


	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}


	public Date getDueDate() {
		return dueDate;
	}


	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}


	public String getDocumentNumber() {
		return documentNumber;
	}


	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
}
