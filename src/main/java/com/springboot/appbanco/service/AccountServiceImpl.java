package com.springboot.appbanco.service;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.springboot.appbanco.controller.AccountController;
import com.springboot.appbanco.model.BankAccount;
import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.repo.IAccountRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements IAccountService {
	
	
	private static Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

	// Inyectar nuestro Cliente

	@Autowired
	@Qualifier("client")
	private WebClient wCClient;
	
	@Autowired
	@Qualifier("personAutho")
	private WebClient wCPersoAutho;

	@Autowired
	IAccountRepo repo;
	
	
	@Value("${valor.perfil.vip}")
	private double vPerfilVip;
	
	@Value("${valor.perfil.pyme}")
	private double vPerfilPyme;
	
	@Value("${valor.perfil.corp}")
	private double vPerfilCorporative;

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
			
			
			//Cuentas iniales con 0
			Double balanceP = new Double(account.getBalance());
			if( balanceP == null ){
				account.setBalance(0.0);
			}
			if( new Double(account.getMinBalanceEndMonth()) == null ){
				account.setMinBalanceEndMonth(0.0);
			}
			
			
			Date date = new Date();
			account.setOpeningDate(date);
			
			if(typeC.equals("Personal" ) || typeC.equals("Persona VIP")) {
				System.out.println("El tipo es Personal o VIP");
				//Solo 1 debe tener de cada tipo:: (3 CB)
				
				
				Mono<Boolean> vB = FluxValidarDNIExistentes(typeAccountl,listaCLientesNuevos).reduce(true, (a, b) -> a & b);
				// Falso...
				
				return vB.flatMap(b -> {
					if (b) {
						// System.out.println("Ya puede registrar");

						
						
						
						
						
						
						/* Registrando en MS CLiente.... Datos de la cuenta, Lista de Clientes (FILAS)*/
						return Mono.just(account).flatMap( objC ->{
							//monto minimo, validar tipo de Perfil (Persona Vip,PYME,Corporative)
							//objC.get
							boolean statusVIP= false;
							double montoMin=0;
							if(typeC.equals("Persona VIP")){
								montoMin = vPerfilVip;
								statusVIP = true;
							}
							/*else if(typeC.equals("PYME")){
								montoMin = vPerfilPyme;
							}else if(typeC.equals("Corporative")) {
								montoMin = vPerfilCorporative;
							}*/
							log.info("Monto Minimo:"+montoMin);
							
							if(objC.getBalance() >= montoMin) {
								
								if(statusVIP) {
									if( objC.getMinBalanceEndMonth() > 0) {
										
										return wCClient.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
												.syncBody(objC).retrieve().bodyToFlux(BankAccount.class)
												.next()
												.flatMap(obj -> {
															return wCPersoAutho.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
																	.syncBody(objC).retrieve().bodyToFlux(BankAccount.class)
																	.next() //Convierte de Flux a Mono.
																	.flatMap(client ->{
																		return repo.save(account);
																	});
														});
										
										
									}else {
										log.info("Falta definir el Monto minimo Mensual");
									}
									
									
								}else {
									//Cuenta Personal:
									//Flux:
									return wCClient.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
									.syncBody(objC).retrieve().bodyToFlux(BankAccount.class)
									.next()
									.flatMap(obj -> {
												return wCPersoAutho.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
														.syncBody(objC).retrieve().bodyToFlux(BankAccount.class)
														.next() //Convierte de Flux a Mono.
														.flatMap(client ->{
															return repo.save(account);
														});
											});
								}
								
								
								
							}else {
								log.info("Es saldo de creacion es menor al Minimo..");
								return Mono.empty();
							}
							
							return Mono.empty();
							
							
									
						});
						
					} else {
						
						//return new ResponseEntity<Page<Genero>>(pacientes, HttpStatus.OK);
						System.out.println("Ya existe");
						 return Mono.empty();
						
						//throw new ModeloNotFoundException("Ya existe almenos 1 Cliente que desea registrar");
					}

				});

				
				
				
				
			}else if(typeC.equals("Empresarial") || typeC.equals("PYME") || typeC.equals("Corporative")){
				System.out.println("El tipo es Empresarial");
				
				if(typeAccountl.equals("Ahorro")) {
					System.out.println("No puede tener una cuenta.");
				}else if(typeAccountl.equals("Plazo")) {
					System.out.println("No puede tener una cuenta");
				}else {
					return Mono.just(account).flatMap( objC ->{
						
						
						
						//monto minimo, validar tipo de Perfil (PYME,Corporative)
						//objC.get
						boolean statusVIP= false;
						double montoMin=0;
						if(typeC.equals("PYME")){
							montoMin = vPerfilPyme;
							statusVIP = true;
						}else if(typeC.equals("Corporative")){
							montoMin = vPerfilCorporative;
							statusVIP = true;
						}
						
						if(objC.getBalance() >= montoMin) {
							
							if(statusVIP) {
								if( objC.getMinBalanceEndMonth() > 0) {
									
									return wCClient.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
											.syncBody(objC).retrieve().bodyToFlux(BankAccount.class)
											.next()
											.flatMap(obj -> {
														return wCPersoAutho.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
																.syncBody(objC).retrieve().bodyToFlux(BankAccount.class)
																.next() //Convierte de Flux a Mono.
																.flatMap(client ->{
																	return repo.save(account);
																});
													});
									
									
								}else {
									log.info("Falta definir el Monto minimo Mensual");
								}
								
								
							}else {
								//Cuenta Personal:
								//Flux:
								return wCClient.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
										.syncBody(objC).retrieve().bodyToFlux(BankAccount.class)
										.next()
										.flatMap(obj -> {
													return wCPersoAutho.post().accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
															.syncBody(objC).retrieve().bodyToFlux(BankAccount.class)
															.next() //Convierte de Flux a Mono.
															.flatMap(client ->{
																return repo.save(account);
															});
												});
							}
							
							
							
						}else {
							log.info("Es saldo de creacion es menor al Minimo..");
							return Mono.empty();
						}
						
						return Mono.empty();
						
						
								
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
