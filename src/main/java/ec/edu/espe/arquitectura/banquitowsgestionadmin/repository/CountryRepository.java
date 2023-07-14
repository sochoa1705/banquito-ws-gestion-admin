package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CountryRepository extends MongoRepository<Country, String> {
    Country findByPhoneCode(String phoneCode);
    Country findFirstByName(String name);


}
