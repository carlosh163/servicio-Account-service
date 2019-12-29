package com.springboot.appbanco.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.appbanco.exception.ModeloNotFoundException;
import com.springboot.appbanco.model.BankAccount;
import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.service.IAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RefreshScope
@RestController
public class AccountController {
	
	
	private static Logger log = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private Environment env;
	@Autowired
	private IAccountService service;

	@Value("${configuracion.texto}")
	private String texto;
	
	
	
	@GetMapping("/obtener-config")
	public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto){
		log.info(texto);
		Map<String,String> json =  new HashMap<>();
		json.put("texto", texto);
		json.put("puerto", puerto);
		
		if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("auto.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("auto.email", env.getProperty("configuracion.autor.email"));
		}
		
		 return new ResponseEntity<Map<String,String>>(json,HttpStatus.OK);
	}
	
	
	@GetMapping
	public Flux<BankAccount> findAll(){
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public Mono<BankAccount> findById(@PathVariable String id){
		
		//Mono<Account> accountE = service.findById(id);
		//accountE.empty()
		
		//return service.findById(id);
		//System.out.println("resp"+accountE);
		
		/*return accountE.flatMap(account ->{
			if(account == null) {
				return Mono.error(new ModeloNotFoundException("ID NO ENCONTRADO"+ id));
			}
		}).switchIfEmpty(Mono.just(accountE));
		
		if(accountE == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO"+ id);
		}*/
		
		//return accountE;
		return service.findById(id);
	}
	
	@PostMapping
	public Mono<BankAccount> create(@RequestBody BankAccount account){
		return service.create(account);
	}
	
	
	@PutMapping("/{id}")
	public Mono<BankAccount> update(@RequestBody BankAccount perso, @PathVariable String id){
		return service.update(perso, id);
	}
	
	@DeleteMapping("/{id}")
	public Mono<Void> delete(@PathVariable String id){
		return service.delete(id);
	}
	
	@GetMapping("/ListarClientes")
	public Flux<Client> findAllClients(){
		return service.findAllClients();
	}
	
	//Consumo CLIENTE::
	
	@GetMapping("/ListarClientesXNroDocu/{nroDoc}")
	public Mono<Client> findClientNrDocu(@PathVariable String nroDoc){
		return service.findClientByNroDoc(nroDoc);
	}
	
	//
	
	@GetMapping("/ListarClientesXNroDocuLocal/{nroDoc}")
	public Flux<BankAccount> findClientNrDocuL(@PathVariable String nroDoc){
		return service.findClienteByNroDocAccount(nroDoc);
	}
	
	
	//Consumo Trans:
	
	@PutMapping("/updateBalanceAccountByAccountNumber/{accountNumber}/{quantity}")
	public Mono<BankAccount> updateBalanceAccountByAccountNumber(@PathVariable Integer accountNumber,@PathVariable double quantity){
		
		return service.findAccountByNroAccount(accountNumber).flatMap(account ->{
			account.setBalance(account.getBalance()+quantity);
			return service.save(account);
		});
		
	}
	
	@PutMapping("/updateBalanceAccountRetireByAccountNumber/{accountNumber}/{quantity}")
	public Mono<BankAccount> updateBalanceAccountRetireByAccountNumber(@PathVariable Integer accountNumber,@PathVariable double quantity){
		
		return service.findAccountByNroAccount(accountNumber).flatMap(account ->{
			if(account.getBalance()-quantity>=0) {
				account.setBalance(account.getBalance()-quantity);
				return service.save(account);
			}else {
				System.out.println("Error saldo insuficiente..");
				return Mono.empty();
			}
			
		});
		
	}
	
	//Pago de una CC desde mi CB.return service.findAccountByNroAccount(accountNumber)
	
	@GetMapping("/findAccountByNumberAccount/{numAcc}")
	public Mono<BankAccount> findClientNrDocuL(@PathVariable Integer numAcc){
		return service.findAccountByNroAccount(numAcc);
	}
}
