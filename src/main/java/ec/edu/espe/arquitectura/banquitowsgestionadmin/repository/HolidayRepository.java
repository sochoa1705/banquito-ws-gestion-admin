package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;

import java.util.Date;
import java.util.List;


public interface HolidayRepository extends MongoRepository<Holiday, String> {
    List<Holiday> findByNameLike(String name);
    List<Holiday> findByType(String type);

    List<Holiday> findByHolidayDateBetween(Date startDate, Date endDate);

    Holiday findByUniqueId(String uniqueId);

    Holiday findByUniqueIdAndState(String uniqueId, String state);
}
