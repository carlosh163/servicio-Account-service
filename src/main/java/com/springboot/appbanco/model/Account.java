package com.springboot.appbanco.model;

import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "account")
public class Account {

	@Id
	private String codAccount;
	
	@Size(min = 3,message ="Tipo de Producto debe tener minimo 3 caracteres")
	private String ProductType; // Cuenta Bancarias o Producto Credito.
	
	private String AccountType; // C.B = Ahorro, CU.Corriente, CU.PlazoFijo. -- CRED = Personal, Empresarial, Tarjeta Credito, Adelanto Efectivo
	
	private List<Client> customerList; //Todos los Clientes que poseen una cuenta
	
	//private List<headline> headlinesList; // Todos los Titulares..
	
	private List<Person> PersonAuthorizedList;
	
	private double saldo;
	private char state; //Activo o Inactivo.
	
	
	public String getCodAccount() {
		return codAccount;
	}
	public void setCodAccount(String codAccount) {
		this.codAccount = codAccount;
	}
	public String getProductType() {
		return ProductType;
	}
	public void setProductType(String productType) {
		ProductType = productType;
	}
	public String getAccountType() {
		return AccountType;
	}
	public void setAccountType(String accountType) {
		AccountType = accountType;
	}
	public List<Client> getCustomerList() {
		return customerList;
	}
	public void setCustomerList(List<Client> customerList) {
		this.customerList = customerList;
	}
	public List<Person> getPersonAuthorizedList() {
		return PersonAuthorizedList;
	}
	public void setPersonAuthorizedList(List<Person> personAuthorizedList) {
		PersonAuthorizedList = personAuthorizedList;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	public char getState() {
		return state;
	}
	public void setState(char state) {
		this.state = state;
	}
	
	
	
}
