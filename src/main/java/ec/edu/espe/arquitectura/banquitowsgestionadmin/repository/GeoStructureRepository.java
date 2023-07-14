package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoStructure;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GeoStructureRepository extends MongoRepository<GeoStructure, String> {
    GeoStructure findGeoStructureByLocationsContaining(List<GeoLocation> locations);

}
