package ec.edu.espe.arquitectura.banquitowsgestionadmin.repository;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoStructure;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GeoStructureRepository extends MongoRepository<GeoStructure, String> {
    @Query(value = "{'locations._id': ?0}", fields = "{'levelCode': 1, 'name': 1,'locations.$': 1}")
    GeoStructure findGeoStructureByLocations(String locationId);

}
