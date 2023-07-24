package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoStructure;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.CountryRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoStructureRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class GeoStructureService {

    private final GeoStructureRepository geoStructureRepository;
    private final GeoLocationRepository geoLocationRepository;
    private final CountryRepository countryRepository;
    private final GeoLocationService geoLocationService;

    public GeoStructureService(GeoStructureRepository geoStructureRepository, GeoLocationRepository geoLocationRepository, CountryRepository countryRepository, GeoLocationService geoLocationService) {
        this.geoStructureRepository = geoStructureRepository;
        this.geoLocationRepository = geoLocationRepository;
        this.countryRepository = countryRepository;
        this.geoLocationService = geoLocationService;
    }

    public void createGeoStructure(GeoStructureRQ geoStructureRQ, String countryCode) {
        try {
            GeoStructure geoStructure = transformGeoStructureRQ(geoStructureRQ);
            Country country = this.countryRepository.findByCode(countryCode);
            switch (geoStructure.getLevelCode()) {
                case 1 -> {
                    List<GeoLocation> provinceList = this.geoLocationRepository.findGeoLocationByZipCode("170101");
                    geoStructure.setLocations(provinceList);
                    geoStructure.setState("ACT");
                    geoStructure.setCountry(country);
                    this.geoStructureRepository.save(geoStructure);
                }
                case 2 -> {
                    List<GeoLocation> cantonList = this.geoLocationRepository.findGeoLocationByZipCode("200202");
                    geoStructure.setLocations(cantonList);
                    geoStructure.setState("ACT");
                    geoStructure.setCountry(country);
                    this.geoStructureRepository.save(geoStructure);
                }
                case 3 -> {
                    List<GeoLocation> parishList = this.geoLocationRepository.findGeoLocationByZipCode("230303");
                    geoStructure.setLocations(parishList);
                    geoStructure.setState("ACT");
                    geoStructure.setCountry(country);
                    this.geoStructureRepository.save(geoStructure);
                }
                default -> throw new RuntimeException("Error al crear la estructura geográfica");
            }

        } catch (RuntimeException rte) {
            throw new RuntimeException("Error al crear la estructura geográfica", rte);
        }
    }

    public GeoStructureRS obtainStructureFromCountry(Integer levelCode, String countryCode) {
        try {
            GeoStructure geoStructure = this.geoStructureRepository.findByLevelCodeAndCountryCode(levelCode, countryCode);
            GeoStructureRS response = responseGeoStructure(geoStructure);
            return response;
        } catch (RuntimeException rte) {
            throw new RuntimeException("Error al obtener las provincias del país", rte);
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
                .country(geoStructure.getCountry())
                .locations(geoStructure.getLocations())
                .build();
        return geoStructureRS;
    }


}
