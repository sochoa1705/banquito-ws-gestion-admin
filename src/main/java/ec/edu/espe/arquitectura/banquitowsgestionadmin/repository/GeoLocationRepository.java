package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface GeoLocationRepository extends MongoRepository<GeoLocation, String> {
    List<GeoLocation> findGeoLocationByZipCode(String zipCode);
    Optional<GeoLocation> findGeoLocationByLocationParentAndName(String locationParent, String name);
}