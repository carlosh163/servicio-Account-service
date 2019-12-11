package com.springboot.appbanco.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	private WebClient client;

	@Autowired
	IAccountRepo repo;

	@Override
	public Flux<Account> findAll() {
		return repo.findAll();
	}

	@Override
	public Mono<Account> findById(String id) {
		// TODO Auto-generated method stub

		/*
		 * 
		 * 
		 * Mono<Beneficiary> found =
		 * beneficiaryRepository.findBeneficiariesByName(requestDTO.getBeneficiaryName()
		 * );
		 * 
		 * return found.flatMap(beneficiary-> { if(beneficiary== null) return
		 * Mono.empty(); // never called else return Mono.error(new
		 * RuntimeException("Already exist " + beneficiary.getId()));
		 * 
		 * }).switchIfEmpty(Mono.just(requestDTO.getBeneficiaryObjectToSave())); // new
		 * object here which want to save.
		 * 
		 */

		// return repo.findById(id);

		/*
		 * Mono<Account> accountE= repo.findById(id); return accountE.flatMap(account
		 * ->{ if(account == null) throw new ModeloNotFoundException("ID NO ENCONTRADO"+
		 * id); else return repo.findById(id); });
		 */

		// this.Account = Optional.ofNullable(this.Account);
		return repo.findById(id);
	}

	// REQ03 : Validacion de Cuenta Unica- Ahorro.,
	@Override
	public Mono<Account> create(Account account) {
		System.out.println("CUENTAAA");

		// Flux<Account> listaCuentas= repo.findAll();

		// listaCuentas.flatMap(cuenta ->{

		List<Client> listaCLientesNuevos = account.getCustomerList();

		String DNIE = "";
		for (Client listaC : listaCLientesNuevos) {
			DNIE = listaC.getNroDocumento();

			System.out.println("DNI " + DNIE); // 47 48
		}

		listaCLientesNuevos.forEach(listaCLientesN -> {

		});

		System.out.println(DNIE);
		Account objcuenta = new Account();
		objcuenta.setState('N');
		Mono<Account> datosCuenta2 = Mono.just(objcuenta);

		return repo.findByAccountXDocument(DNIE)
				.switchIfEmpty(datosCuenta2)
				.flatMap(DatAccountsOp ->{
					
					
					if(DatAccountsOp != null) {
						System.out.println("SE ENCONTRO A ESTE CLIENTE");
					}
					
					if(DatAccountsOp.getState() =='N') {
						System.out.println("Ya puede registrar");
						
					}else {
						System.out.println("Ya existe");
					}
					
					return Mono.empty();
				});

		/*return datosCuenta.flatMap(datAccount -> {

			Integer nroE = datAccount.getAccountNro();
			Integer nroAR = account.getAccountNro();

			System.out.println("nro Encontrado" + nroE);
			System.out.println("nro A registrar" + nroAR);
			if (nroE != null) {
				System.out.println("Ya existe");
			} else {
				System.out.println("Ya puede registrar");
			}
			// account.getCustomerList()
			return Mono.empty();

		});*/

		// return repo.save(account);

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

		return client.get().accept(APPLICATION_JSON_UTF8).exchange()
				.flatMapMany(response -> response.bodyToFlux(Client.class));

	}

	@Override
	public Mono<Client> findByIdClient(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		return client.get().uri("/{id}", params).accept(APPLICATION_JSON_UTF8)
				// .retrieve()
				// .bodyToMono(Producto.class);
				.exchange().flatMap(response -> response.bodyToMono(Client.class));
	}

	@Override
	public Mono<Client> createClient(Client cliente) {

		return client.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
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

		return client.put().uri("/{id}", params).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
				.syncBody(cliente).retrieve().bodyToMono(Client.class);
	}

	@Override
	public Mono<Void> deleteClient(String id) {

		return client.delete().uri("/{id}", Collections.singletonMap("id", id)).exchange().then();
	}

	@Override
	public Mono<Client> findClientByNroDoc(String nroDoc) {

		// Map<String, Object> params = new HashMap<>();
		// params.put("id", id);
		return client.get().uri("/BuscarClientePorNroDoc/{nroDoc}", Collections.singletonMap("nroDoc", nroDoc))
				.accept(APPLICATION_JSON_UTF8).exchange().flatMap(response -> response.bodyToMono(Client.class));
	}

	@Override
	public Mono<Account> findClienteByNroDocAccount(String nroDoc) {

		return repo.findByAccountXDocument(nroDoc);
	}

}
