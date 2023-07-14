package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;

public interface HolidayRepository extends MongoRepository<Holiday,String>{
    
    List<Holiday> findByNameLike(String name);
    List<Holiday> findByType(String type);
    //List<Holiday> findHolidayByCountry_Id(String countryId);
    //List<Holiday> findHolidayByLocationId(String locationId);

}
