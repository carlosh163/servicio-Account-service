package com.springboot.appbanco.repo;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.appbanco.model.Account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IAccountRepo extends ReactiveMongoRepository<Account,String>{

	@Query("{'customerList.documentNumber' : ?0}")
	Flux<Account> findByAccountXDocument(String document);
	
	Flux<Account> findByAccountType(String typeA);
}
