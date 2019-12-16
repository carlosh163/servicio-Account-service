package com.springboot.appbanco.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.springframework.http.MediaType.*;
import org.springframework.stereotype.Service;
import static org.springframework.web.reactive.function.BodyInserters.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import com.springboot.appbanco.exception.ModeloNotFoundException;
import com.springboot.appbanco.model.Account;
import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.repo.IAccountRepo;

import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements IAccountService {

	// Inyectar nuestro Cliente

	@Autowired
	@Qualifier("client")
	private WebClient wCClient;
	
	
	
	
	@Autowired
	@Qualifier("personAutho")
	private WebClient wCPersoAutho;

	@Autowired
	IAccountRepo repo;

	@Override
	public Flux<Account> findAll() {
		return repo.findAll();
	}

	@Override
	public Mono<Account> findById(String id) {
		return repo.findById(id);
	}

	// REQ03 : Validacion de Cuenta Unica- Ahorro.,
	@Override
	public Mono<Account> create(Account account) {
		System.out.println("CUENTAAA");

		// Aperturar una Cuenta Ahorro.. MSAhorro. DATOS cuenta
		// (nroCuenta,SALDO,fechaApert..) List<Client> objClient.
		// OBJETIVO: Identificar si los DNI de los Clientes son nuevos....
		List<Client> listaCLientesNuevos = account.getCustomerList();
		Mono<Boolean> vB = FluxValidarDNIExistentes(listaCLientesNuevos).reduce(true, (a, b) -> a & b);
		// Falso...
		return vB.flatMap(b -> {
			if (b) {
				// System.out.println("Ya puede registrar");

				Date date = new Date();

				account.setOpeningDate(date);
				/*Map<String, Object> params = new HashMap<>();  Map<String, Object>
				params.put("Estado", "Se Registro con exito");*/

				//params.put("detail-Create", repo.save(account).subscribe());
				
				
				
				
				
				//Enviar una lista de Clientes..
				//Mono<Client> mCliente = 
				
				
				/*Flux.fromIterable(listaCLientesNuevos).flatMap(tclient -> {

					//Datos para la cuenta:
					tclient.set
					account
					
					
					
					return Flux.empty();

				});*/
				
				/* Registrando en MS CLiente.... Datos de la cuenta, Lista de Clientes (FILAS)*/
				return Flux.just(account).flatMap( objC ->{
						//Flux:
					
					return wCPersoAutho.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
							.syncBody(objC).retrieve().bodyToFlux(Account.class)
							.next()
							.map(objClie -> {
								return wCClient.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
										.syncBody(objC).retrieve().bodyToFlux(Account.class);
							});
				}).next() //Convierte de Flux a Mono.
						.flatMap(objClient ->{
							return repo.save(account);
						});
					

			} else {
				System.out.println("Ya existe");

				/*Map<String, Object> params = new HashMap<>();
				params.put("Estado", "Ya existe titular (es)");*/

				//return Mono.just(params);
				
				 return Mono.empty();
			}

			// return Mono.empty();
		});

	}

	private Flux<Boolean> FluxValidarDNIExistentes(List<Client> list) {
		// boolean estadoF= false;

		return Flux.fromIterable(list).flatMap(client -> {

			String DNI = client.getDocumentNumber();

			Account objcuenta = new Account();
			objcuenta.setAccountstatus('N');

			return repo.findByAccountXDocument(DNI).switchIfEmpty(Mono.just(objcuenta)).map(DatAccountsOp -> {
				if (DatAccountsOp.getAccountstatus() == 'N') {
					// System.out.println("Ya puede registrar");
					return true;

				} else {
					// System.out.println("Ya existe");
					return false;
				}

				// return estadoF;
			});

		});

	}

	@Override
	public Mono<Account> update(Account account, String id) {
		// TODO Auto-generated method stub
		/*
		 * return repo.findById(id).flatMap(Account ->{
		 * 
		 * Account.setNombres(persoClie.getNombres());
		 * Account.setApellidos(persoClie.getApellidos());
		 * Account.setTipoDocumento(persoClie.getTipoDocumento());
		 * Account.setNroDocumento(persoClie.getNroDocumento());
		 * Account.setEstado(persoClie.getEstado());
		 * //Account.setAccountsList(accountsList); return repo.save(Account); });
		 */
		return Mono.error(new Throwable());
	}

	@Override
	public Mono<Void> delete(String id) {
		// TODO Auto-generated method stub
		return repo.findById(id).flatMap(account -> repo.delete(account));
	}

	// Consumiendo servicios de otro MS.
	@Override
	public Flux<Client> findAllClients() {

		return wCClient.get().accept(APPLICATION_JSON_UTF8).exchange()
				.flatMapMany(response -> response.bodyToFlux(Client.class));

	}

	@Override
	public Mono<Client> findByIdClient(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return wCClient.get().uri("/{id}", params).accept(APPLICATION_JSON_UTF8)
				// .retrieve()
				// .bodyToMono(Producto.class);
				.exchange().flatMap(response -> response.bodyToMono(Client.class));
	}

	@Override
	public Mono<Client> createClient(Client cliente) {

		return wCClient.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
				// .body(fromObject(cliente)) - -BodyInserters
				.syncBody(cliente).retrieve().bodyToMono(Client.class);
	}

	@Override
	public Mono<Client> updateClient(Client cliente, String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);

		/*
		 * Collections.singletonMap("id",id)
		 */

		return wCClient.put().uri("/{id}", params).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
				.syncBody(cliente).retrieve().bodyToMono(Client.class);
	}

	@Override
	public Mono<Void> deleteClient(String id) {

		return wCClient.delete().uri("/{id}", Collections.singletonMap("id", id)).exchange().then();
	}

	@Override
	public Mono<Client> findClientByNroDoc(String nroDoc) {

		// Map<String, Object> params = new HashMap<>();
		// params.put("id", id);
		return wCClient.get().uri("/BuscarClientePorNroDoc/{nroDoc}", Collections.singletonMap("nroDoc", nroDoc))
				.accept(APPLICATION_JSON_UTF8).exchange().flatMap(response -> response.bodyToMono(Client.class));
	}

	@Override
	public Mono<Account> findClienteByNroDocAccount(String nroDoc) {

		return repo.findByAccountXDocument(nroDoc);
	}

}
