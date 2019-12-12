package com.springboot.appbanco.service;

import java.util.Map;

import com.springboot.appbanco.model.Account;
import com.springboot.appbanco.model.Client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {

	public Flux<Account> findAll();

	public Mono<Account> findById(String id);

	public Mono<Map<String,Object>> create(Account account);

	public Mono<Account> update(Account account, String id);

	public Mono<Void> delete(String id);
	
	//Metodos para Client:
	public Flux<Client> findAllClients();
	public Mono<Client> findByIdClient(String id);
	public Mono<Client> createClient(Client client);
	public Mono<Client> updateClient(Client client, String id);
	public Mono<Void> deleteClient(String id);
	
	
	public Mono<Client> findClientByNroDoc(String nroDoc); //Consumo A cliente.
	public Mono<Account> findClienteByNroDocAccount(String nroDoc); //Consulta al documento Account
}
