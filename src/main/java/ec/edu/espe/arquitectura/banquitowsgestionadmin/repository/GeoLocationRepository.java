package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface GeoLocationRepository extends MongoRepository<GeoLocation, String> {
    List<GeoLocation> findGeoLocationByZipCode(String zipCode);

}
