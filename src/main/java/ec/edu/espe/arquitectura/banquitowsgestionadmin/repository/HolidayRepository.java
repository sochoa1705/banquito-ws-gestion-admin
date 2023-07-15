package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;
import java.util.List;


public interface HolidayRepository extends MongoRepository<Holiday, String> {
    List<Holiday> findByNameLike(String name);
}
