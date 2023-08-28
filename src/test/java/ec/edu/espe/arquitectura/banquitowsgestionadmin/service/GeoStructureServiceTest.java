package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoStructure;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.CountryRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoStructureRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GeoStructureServiceTest {
    @Mock
    private CountryRepository countryRepository;

    @Mock
    private GeoLocationRepository geoLocationRepository;

    @Mock
    private GeoStructureRepository geoStructureRepository;

    @InjectMocks
    private GeoStructureService geoStructureService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void GeoStructureService_CreateGeoStructure_ReturnVoid() {

        GeoStructureRQ geoStructureRQ = new GeoStructureRQ(1,"Provincia");
        String countryCode = "ECU";

        Country country = new Country("1","ECU","Ecuador","+593","ACT");
        GeoLocation geoLocation1 = GeoLocation.builder()
                .name("Pichincha")
                .areaPhoneCode("02")
                .zipCode("170101")
                .locationParent("ECU")
                .build();
        GeoLocation geoLocation2 = GeoLocation.builder()
                .name("Azuay")
                .areaPhoneCode("02")
                .zipCode("170101")
                .locationParent("ECU")
                .build();

        List<GeoLocation> geoLocations = List.of(geoLocation1, geoLocation2);

        GeoStructure geoStructure = new GeoStructure("1",1,"Provincia","ACT",country,geoLocations);

        when(countryRepository.findByCode(countryCode)).thenReturn(country);
        when(geoLocationRepository.findGeoLocationByZipCodeAndLocationParent(eq("170101"), eq(countryCode))).thenReturn(geoLocations);
        when(geoStructureRepository.save(any(GeoStructure.class))).thenReturn(geoStructure);

        // Act
        geoStructureService.createGeoStructure(geoStructureRQ, countryCode);

        // Assert
        verify(geoStructureRepository, times(1)).save(any(GeoStructure.class));
    }

    @Test
    public void GeoStructureService_ObtainStructureFromCountry_ReturnGeoStructureRS(){

        Integer levelCode = 1;
        String countryCode = "ECU";
        Country country = new Country("1","ECU","Ecuador","+593","ACT");
        GeoLocation geoLocation1 = GeoLocation.builder()
                .name("Pichincha")
                .areaPhoneCode("02")
                .zipCode("170101")
                .locationParent("ECU")
                .build();
        GeoLocation geoLocation2 = GeoLocation.builder()
                .name("Azuay")
                .areaPhoneCode("02")
                .zipCode("170101")
                .locationParent("ECU")
                .build();

        List<GeoLocation> geoLocations = List.of(geoLocation1, geoLocation2);

        GeoStructure geoStructure = new GeoStructure("1",1,"Provincia","ACT",country,geoLocations);
        GeoStructureRS response = new GeoStructureRS(levelCode, "Provincia", country, geoLocations);

        when(geoStructureRepository.findByLevelCodeAndCountryCode(levelCode, countryCode)).thenReturn(geoStructure);

        when(geoStructureRepository.findByLevelCodeAndCountryCode(levelCode, countryCode)).thenReturn(geoStructure);


        GeoStructureRS result = geoStructureService.obtainStructureFromCountry(levelCode, countryCode);


        assertEquals(response, result);
    }


}
