package com.springboot.appbanco.service;

import com.springboot.appbanco.model.BankAccount;
import com.springboot.appbanco.model.Client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {

	public Flux<BankAccount> findAll();

	public Mono<BankAccount> findById(String id);

	public Mono<BankAccount> create(BankAccount account);

	public Mono<BankAccount> update(BankAccount account, String id);

	public Mono<Void> delete(String id);
	
	//Metodos para Client:
	public Flux<Client> findAllClients();
	public Mono<Client> findByIdClient(String id);
	public Mono<Client> createClient(Client client);
	public Mono<Client> updateClient(Client client, String id);
	public Mono<Void> deleteClient(String id);
	
	
	public Mono<Client> findClientByNroDoc(String nroDoc); //Consumo A cliente.
	public Flux<BankAccount> findClienteByNroDocAccount(String nroDoc); //Consulta al documento Account
	
	public Mono<BankAccount> findAccountByNroAccount (Integer accNumber);
	
	public Mono<BankAccount> save(BankAccount account);
}
