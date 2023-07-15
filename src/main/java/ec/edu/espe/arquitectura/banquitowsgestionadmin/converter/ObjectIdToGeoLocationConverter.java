package ec.edu.espe.arquitectura.banquitowsgestionadmin.converter;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;

public class ObjectIdToGeoLocationConverter implements Converter<ObjectId, GeoLocation> {

    private final GeoLocationRepository geoLocationRepository;

    public ObjectIdToGeoLocationConverter(GeoLocationRepository geoLocationRepository) {
        this.geoLocationRepository = geoLocationRepository;
    }

    @Override
    public GeoLocation convert(ObjectId source) {
        String locationId = source.toHexString();
        return geoLocationRepository.findById(locationId).orElse(null);
    }
}
