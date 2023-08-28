package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.CountryRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.CountryRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.CountryRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
class CountryServiceTest {
    @InjectMocks
    private CountryService countryService;

    @Mock
    private CountryRepository countryRepository;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    //Test
    @Test
    void testObtainCountry(){
        //Given
        String countryCode = "EC";
        Country country = new Country().builder()
                .code("EC")
                .name("Ecuador")
                .phoneCode("593")
                .build();
        given(countryRepository.findByCode(countryCode)).willReturn(country);
        //When
        CountryRS result = countryService.obtain(countryCode);
        //Then
        assertEquals(country.getCode(), result.getCode());
        assertEquals(country.getName(), result.getName());
        assertEquals(country.getPhoneCode(), result.getPhoneCode());
        verify(countryRepository).findByCode(countryCode);
    }
    @Test
    void testFindCountryByName() {
        // Given
        String countryName = "Ecuador";
        Country country = new Country().builder()
                .code("EC")
                .name("Ecuador")
                .phoneCode("593")
                .build();

        given(countryRepository.findByName(countryName)).willReturn(country);
        // When
        CountryRS result = countryService.obtainByName(countryName);
        // Then
        assertEquals(country.getCode(), result.getCode());
        assertEquals(country.getName(), result.getName());
        assertEquals(country.getPhoneCode(), result.getPhoneCode());
        verify(countryRepository).findByName(countryName);
    }

    @Test
    void testGetAllCountries() {
        // Given
        Country country1 = Country.builder()
                .code("EC")
                .name("Ecuador")
                .phoneCode("593")
                .build();
        Country country2 = Country.builder()
                .code("CO")
                .name("Colombia")
                .phoneCode("593")
                .build();
        given(countryRepository.findAll()).willReturn(List.of(country1, country2));

        // When
        List<CountryRS> result = countryService.getAllCountries();

        // Then
        assertEquals(2, result.size());
        assertEquals(country1.getCode(), result.get(0).getCode());
        assertEquals(country2.getCode(), result.get(1).getCode());
        verify(countryRepository).findAll();
    }

    //
    @Test
    void testLogicDeleteCountry() {
        // Given
        Country country = Country.builder()
                .code("EC")
                .name("Ecuador")
                .phoneCode("593")
                .status("INA")
                .build();
        given(countryRepository.findByCode(country.getCode())).willReturn(country);

        // When
        CountryRS result = countryService.logicDelete(country.getCode());

        // Then
        assertEquals(country.getCode(), result.getCode());
        assertEquals(country.getName(), result.getName());
        assertEquals(country.getPhoneCode(), result.getPhoneCode());
        assertEquals("INA", country.getStatus()); // Check status has been changed
        verify(countryRepository).findByCode(country.getCode());
        verify(countryRepository).save(country);
    }
    @Test
    void testLogicDeleteNonExistingCountryThrowsRTE() {
        // Given
        String countryCode = "EC";
        given(countryRepository.findByCode(countryCode)).willReturn(null);  // Simulate non-existing country

        // When and Then
        assertThrows(RuntimeException.class, () -> countryService.logicDelete(countryCode));
        verify(countryRepository).findByCode(countryCode);  // Verify the interaction
    }

    // UpdateCountryTest
    @Test
    void testUpdateCountry() {
        // Given
        CountryRQ countryRQ = new CountryRQ();
        countryRQ.setCode("EC");
        countryRQ.setName("Ecuador");
        countryRQ.setPhoneCode("593");

        Country countryToUpdate = new Country();
        countryToUpdate.setCode("EC");
        countryToUpdate.setName("New Ecuador");
        countryToUpdate.setPhoneCode("123");

        given(countryRepository.findByCode("EC")).willReturn(countryToUpdate);

        // When
        CountryRS result = countryService.updateCountry(countryRQ);

        // Then
        assertEquals(countryRQ.getCode(), result.getCode());
        assertEquals(countryRQ.getName(), result.getName());
        assertEquals(countryRQ.getPhoneCode(), result.getPhoneCode());

        verify(countryRepository).findByCode("EC");
        verify(countryRepository).save(countryToUpdate);
    }
    // UpdateCountryTest
    @Test
    void testUpdateNonExistingCountryThrowsRTE (){
        // Given
        CountryRQ countryRQ = new CountryRQ();
        countryRQ.setCode("EC");
        countryRQ.setName("Ecuador");
        countryRQ.setPhoneCode("593");

        given(countryRepository.findByCode("EC")).willReturn(null);

        // When and Then
        assertThrows(RuntimeException.class, () -> countryService.updateCountry(countryRQ));
        verify(countryRepository).findByCode("EC");

    }
}