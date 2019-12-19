package com.springboot.appbanco.service;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.springboot.appbanco.model.BankAccount;
import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.repo.IAccountRepo;

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
	public Flux<BankAccount> findAll() {
		return repo.findAll();
	}

	@Override
	public Mono<BankAccount> findById(String id) {
		return repo.findById(id);
	}

	// REQ03 : Validacion de Cuenta Unica- Ahorro.,
	@Override
	public Mono<BankAccount> create(BankAccount account) {
		System.out.println("CUENTAAA");

		// Aperturar una Cuenta Ahorro.. MSAhorro. DATOS cuenta (nroCuenta,SALDO,fechaApert..) List<Client> objClient.
		// OBJETIVO: Identificar si los DNI de los Clientes son nuevos....
		List<Client> listaCLientesNuevos = account.getCustomerList();
		
		//Validando segun el Tipo de Cliente: (Personal)
		
		return Flux.fromIterable(listaCLientesNuevos).flatMap(client -> {
		 //TypeClient::
			
			
			
			return Flux.just(client);
		})
		.next()
		.flatMap(objClient ->{
			
		
			System.out.println("Ingreso a ver 1 Cliente,tipo");
			String typeC = objClient.getClientType();
			String typeAccountl = account.getAccountType();
			if(typeC.equals("Personal")) {
				System.out.println("El tipo es Personal");
				//Solo 1 debe tener de cada tipo:: (3 CB)
				
				
				Mono<Boolean> vB = FluxValidarDNIExistentes(typeAccountl,listaCLientesNuevos).reduce(true, (a, b) -> a & b);
				// Falso...
				
				return vB.flatMap(b -> {
					if (b) {
						// System.out.println("Ya puede registrar");

						Date date = new Date();
						account.setOpeningDate(date);
						
						
						
						/* Registrando en MS CLiente.... Datos de la cuenta, Lista de Clientes (FILAS)*/
						return Flux.just(account).flatMap( objC ->{
								//Flux:
							wCClient.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
							.syncBody(objC).retrieve().bodyToFlux(BankAccount.class).subscribe();
							return wCPersoAutho.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
									.syncBody(objC).retrieve().bodyToFlux(BankAccount.class);
						}).next() //Convierte de Flux a Mono.
								.flatMap(client ->{
									return repo.save(account);
								});
					} else {
						
						//return new ResponseEntity<Page<Genero>>(pacientes, HttpStatus.OK);
						System.out.println("Ya existe");
						 return Mono.empty();
						
						//throw new ModeloNotFoundException("Ya existe almenos 1 Cliente que desea registrar");
					}

				});

				
				
				
				
			}else if(typeC.equals("Empresarial")){
				System.out.println("El tipo es Empresarial");
				
				if(typeAccountl.equals("Ahorro")) {
					System.out.println("No puede tener una cuenta.");
				}else if(typeAccountl.equals("Plazo")) {
					System.out.println("No puede tener una cuenta");
				}else {
					/* Registrando en MS CLiente.... Datos de la cuenta, Lista de Clientes (FILAS)*/
					return Flux.just(account).flatMap( objC ->{
							//Flux:
						wCClient.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
						.syncBody(objC).retrieve().bodyToFlux(BankAccount.class).subscribe();
						return wCPersoAutho.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
								.syncBody(objC).retrieve().bodyToFlux(BankAccount.class);
					}).next() //Convierte de Flux a Mono.
							.flatMap(client ->{
								return repo.save(account);
							});
				}
			}
			
			return Mono.empty();
			
		});
		
		
		
		
	}

	private Flux<Boolean> FluxValidarDNIExistentes(String typeAccountAperture,List<Client> lstclient) {
		// boolean estadoF= false;
		
		return repo.findByAccountType(typeAccountAperture).flatMap(cuentaxType ->{
			
			return Flux.fromIterable(cuentaxType.getCustomerList()).map(client -> {
				boolean estado = true;
				
				for(Client objCliexists: lstclient) {
					if(objCliexists.getDocumentNumber().equals(client.getDocumentNumber())) {
						estado = false;
						break;
					}else {
						estado = true;
					}
				}
				//return Flux.just(client);
				return estado;
				
			});

				/*String DNI = client.getDocumentNumber();

				Account objcuenta = new Account();
				objcuenta.setAccountstatus('N');
				
/*Flux.fromIterable(lstclient).flatMap(clientExist ->{
	
	
	
	
});*/
				/*return Flux.fromIterable(list).flatMap(client -> {

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


				
						return repo.findByAccountXDocument(DNI).switchIfEmpty(Mono.just(objcuenta)).map(DatAccountsOp -> {
					if (DatAccountsOp.getAccountstatus() == 'N') {
						 System.out.println("Ya puede registrar");
						return true;

					} else {
						// System.out.println("Ya existe");
						return false;
					}

					// return estadoF;
				});

			});*/
			
		});
		

		

	}

	@Override
	public Mono<BankAccount> update(BankAccount account, String id) {
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
	public Flux<BankAccount> findClienteByNroDocAccount(String nroDoc) {

		return repo.findByAccountXDocument(nroDoc);
	}

	@Override
	public Mono<BankAccount> findAccountByNroAccount(Integer accNumber) {
		
		return repo.findByAccountNumber(accNumber);
	}

	@Override
	public Mono<BankAccount> save(BankAccount account) {
		
		return repo.save(account);
	}

}
