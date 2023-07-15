package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Branch;

public interface BranchRepository extends MongoRepository <Branch, String> {

    List <Branch> findbyName (String name);
    
}
