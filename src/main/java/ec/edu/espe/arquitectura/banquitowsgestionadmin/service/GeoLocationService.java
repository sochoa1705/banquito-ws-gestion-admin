package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoLocationRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoLocationRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeoLocationService {
    private final GeoLocationRepository geoLocationRepository;

    public GeoLocationService(GeoLocationRepository geoLocationRepository) {
        this.geoLocationRepository = geoLocationRepository;
    }

    public void createGeoLocation(GeoLocationRQ geoLocation){
        GeoLocation location = this.transformLocationRQ(geoLocation);
        this.geoLocationRepository.save(location);
    }

    public List<GeoLocationRS> findLocationByZipCode(String zipCode){
        try {
            List<GeoLocation> geoLocations = this.geoLocationRepository.findGeoLocationByZipCode(zipCode);
            List<GeoLocationRS> geoLocationRSList = new ArrayList<>();
            geoLocations.forEach(geoLocation -> {
                geoLocationRSList.add(responseLocation(geoLocation));
            });
            return geoLocationRSList;
        }catch(RuntimeException rte){
            throw new RuntimeException("Error al obtener la información de la ubicación");
        }
    }


    public GeoLocation transformLocationRQ(GeoLocationRQ rq){
        GeoLocation geoLocation = GeoLocation
                .builder()
                .name(rq.getName())
                .areaPhoneCode(rq.getAreaPhoneCode())
                .zipCode(rq.getZipCode())
                .locationParent(rq.getLocationParent())
                .build();
        return geoLocation;
    }

    public GeoLocationRS responseLocation(GeoLocation geoLocation){
        GeoLocationRS geoLocationRS = GeoLocationRS
                .builder()
                .name(geoLocation.getName())
                .areaPhoneCode(geoLocation.getAreaPhoneCode())
                .zipCode(geoLocation.getZipCode())
                .locationParent(geoLocation.getLocationParent())
                .build();
        return geoLocationRS;
    }
}
