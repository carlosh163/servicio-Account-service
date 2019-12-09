package com.springboot.appbanco.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Document(collection = "client")
public class Client {

	@Id
	private String codCliente;
	
	private String nombres;
	private String apellidos;
	private String tipoCliente; // Personal o Empresarial.
	private String tipoDocumento; //DNI o Carnet de Extrangeria.
	private String nroDocumento; 
	private char estado;
	
	private List<Account> accountsList;
	
	public Client() {
		// TODO Auto-generated constructor stub
	}

	

	public String getCodCliente() {
		return codCliente;
	}



	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}



	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public char getEstado() {
		return estado;
	}

	public void setEstado(char estado) {
		this.estado = estado;
	}



	public List<Account> getAccountsList() {
		return accountsList;
	}



	public void setAccountsList(List<Account> accountsList) {
		this.accountsList = accountsList;
	}



	
	

	


	
	
}
