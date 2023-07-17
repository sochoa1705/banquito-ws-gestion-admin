package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Branch;
import org.springframework.data.mongodb.repository.MongoRepository;


import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.BankEntity;

import java.util.List;

public interface BankEntityRepository extends MongoRepository<BankEntity, String> {
    
    BankEntity findBankEntityByIdAndInternationalCode(String id, String internationalCode);


    //List<BankEntity> findByBranches(String code);

    //List<Branch> findB(String code);

}
