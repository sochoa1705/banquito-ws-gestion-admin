package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoLocationRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GeoLocationServiceTest {
    @Mock
    private GeoLocationRepository geoLocationRepository;
    @InjectMocks
    private GeoLocationService geoLocationService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void GeoLocationService_FindLocationByZipCode_ReturnGeoLocationList(){
        String zipCode = "170101";
        GeoLocation geoLocation = GeoLocation.builder()
                .name("Pichincha")
                .areaPhoneCode("02")
                .zipCode("170101")
                .locationParent("EC")
                .build();

        List<GeoLocation> geoLocations = Arrays.asList(geoLocation);

        when(geoLocationRepository.findGeoLocationByZipCode(zipCode)).thenReturn(geoLocations);

        geoLocationService.findLocationByZipCode(zipCode);

        verify(geoLocationRepository, times(1)).findGeoLocationByZipCode(zipCode);
    }

    @Test(expected = RuntimeException.class)
    public void GeoLocationService_FindLocationByZipCode_WithError() {
        String zipCode = "170101";

        when(geoLocationRepository.findGeoLocationByZipCode(zipCode)).thenThrow(new RuntimeException("Error"));

        geoLocationService.findLocationByZipCode(zipCode);
    }

}
