package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;


import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.BankEntity;

public interface BankEntityRepository extends MongoRepository<BankEntity, String> {
    
    BankEntity findByIdAndInternationalCode(String id, String internationalCode);
}
