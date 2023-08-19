package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;

import java.util.Date;
import java.util.List;


public interface HolidayRepository extends MongoRepository<Holiday, String> {
    List<Holiday> findByNameLike(String name);
    List<Holiday> findByType(String type);

    List<Holiday> findByHolidayDateBetweenAndCountry(Date startDate, Date endDate, Country country);

    Holiday findByUniqueId(String uniqueId);

    Holiday findByUniqueIdAndState(String uniqueId, String state);
}
