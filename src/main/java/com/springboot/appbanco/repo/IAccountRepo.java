package com.springboot.appbanco.repo;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.appbanco.model.Account;

@Repository
public interface IAccountRepo extends ReactiveMongoRepository<Account,String>{

}
