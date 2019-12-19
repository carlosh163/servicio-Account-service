package com.springboot.appbanco.repo;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.appbanco.model.BankAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IAccountRepo extends ReactiveMongoRepository<BankAccount,String>{

	@Query("{'customerList.documentNumber' : ?0}")
	Flux<BankAccount> findByAccountXDocument(String document);
	
	Flux<BankAccount> findByAccountType(String typeA);
	
	Mono<BankAccount> findByAccountNumber(Integer nroAccount);
}
