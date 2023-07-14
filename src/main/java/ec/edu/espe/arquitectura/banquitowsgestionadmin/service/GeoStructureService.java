package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoLocationRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoStructure;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoStructureRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GeoStructureService {

    private final GeoStructureRepository geoStructureRepository;
    private final GeoLocationRepository geoLocationRepository;
    private final GeoLocationService geoLocationService;

    public GeoStructureService(GeoStructureRepository geoStructureRepository, GeoLocationRepository geoLocationRepository, GeoLocationService geoLocationService) {
        this.geoStructureRepository = geoStructureRepository;
        this.geoLocationRepository = geoLocationRepository;
        this.geoLocationService = geoLocationService;
    }

    public void createGeoStructure(GeoStructureRQ geoStructureRQ) {
        try {
            GeoStructure geoStructure = transformGeoStructureRQ(geoStructureRQ);
            switch (geoStructure.getLevelCode()) {
                case 1:
                    List<GeoLocation> provinceList = this.geoLocationRepository.findGeoLocationByZipCode("170101");
                    geoStructure.setLocations(provinceList);
                    geoStructure.setState("ACT");
                    this.geoStructureRepository.save(geoStructure);
                    break;
                case 2:
                    List<GeoLocation> cantonList = this.geoLocationRepository.findGeoLocationByZipCode("200202");
                    geoStructure.setLocations(cantonList);
                    geoStructure.setState("ACT");
                    this.geoStructureRepository.save(geoStructure);
                    break;
                case 3:
                    List<GeoLocation> parishList = this.geoLocationRepository.findGeoLocationByZipCode("230303");
                    geoStructure.setLocations(parishList);
                    geoStructure.setState("ACT");
                    this.geoStructureRepository.save(geoStructure);
                    break;
                default:
                    throw new RuntimeException("Error al crear la estructura geográfica");
            }

        } catch (RuntimeException rte) {
            throw new RuntimeException("Error al crear la estructura geográfica", rte);
        }
    }

    public GeoStructureRS findLocationParent(GeoLocationRQ locationRQ) {
        try {
            GeoLocation locationTemp = this.geoLocationService.transformLocationRQ(locationRQ);
            List<GeoLocation> locationList = new ArrayList<>();
            Optional<GeoLocation> locationOpt = this.geoLocationRepository.findGeoLocationByLocationParentAndName(locationTemp.getLocationParent(), locationTemp.getName());
            if (locationOpt.isPresent()) {
                GeoLocation location = locationOpt.get();
                locationList.add(location);
                return this.responseGeoStructure(this.geoStructureRepository.findGeoStructureByLocationsContaining(locationList));
            } else {
                throw new RuntimeException("Error");
            }
        } catch (RuntimeException rte) {
            throw new RuntimeException("Error al obtener la información de la ubicación");
        }

    }


    public GeoStructure transformGeoStructureRQ(GeoStructureRQ rq) {
        GeoStructure geoStructure = GeoStructure
                .builder()
                .levelCode(rq.getLevelCode())
                .name(rq.getName())
                .build();
        return geoStructure;
    }

    public GeoStructureRS responseGeoStructure(GeoStructure geoStructure) {
        GeoStructureRS geoStructureRS = GeoStructureRS
                .builder()
                .levelCode(geoStructure.getLevelCode())
                .name(geoStructure.getName())
                .locations(geoStructure.getLocations())
                .build();
        return geoStructureRS;
    }


}
