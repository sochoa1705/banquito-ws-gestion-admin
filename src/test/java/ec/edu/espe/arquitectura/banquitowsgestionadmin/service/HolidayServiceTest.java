package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoStructure;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.CountryRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoStructureRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class HolidayServiceTest {

    @Mock
    private HolidayRepository holidayRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private GeoStructureRepository geoStructureRepository;
    @Mock
    private GeoLocationRepository geoLocationRepository;

    @InjectMocks
    private HolidayService holidayService;

    private Holiday holidayTest1;

    private Holiday holidayTest2;

    private List<Holiday> holidayList;
    private HolidayRQ holidayRQ;

    private Country country;
    private GeoLocation location;
    private GeoLocation locatonTest;
    private List<GeoLocation> locationList;
    private GeoStructure structure;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        location = GeoLocation.builder()
                .id("1")
                .name("Quito")
                .areaPhoneCode("123")
                .zipCode("112")
                .build();
        locatonTest = GeoLocation.builder()
                .id("1")
                .name("Sangolqui")
                .areaPhoneCode("123")
                .zipCode("112")
                .build();
        country= Country.builder()
                .id("1")
                .code("ECU")
                .name("ECUADOR")
                .phoneCode("593")
                .status("ACT")
                .build();
        holidayTest1 = Holiday.builder()
                .id("1")
                .uniqueId("e65f0753-fb67-4e1e-8e5d-ca3fdcf81ee8")
                .holidayDate(new Date())
                .location(location)
                .country(country)
                .name("Día de la Independencia")
                .type("NAT")
                .state("ACT")
                .build();

        holidayTest2 = Holiday.builder()
                .id("2")
                .uniqueId("b08b0932-bfe1-4984-84a9-432dda6eff8e")
                .holidayDate(new Date())
                .location(location)
                .country(country)
                .name("Día de Fiesta")
                .type("REG")
                .state("ACT")
                .build();
        holidayList = new ArrayList<>();
        holidayList.add(holidayTest1);
        holidayList.add(holidayTest2);
        holidayRQ = HolidayRQ.builder()
                .holidayDate(new Date())
                .name("Día de Prueba")
                .type("REG")
                .country(country)
                .location(location)
                .build();
        structure = GeoStructure.builder().build();
        structure.setId("1");
        structure.setLevelCode(1);
        structure.setName("Location test");
        structure.setState("ACT");
        structure.setCountry(country);
        locationList = new ArrayList<>();
        locationList.add(location);
        locationList.add(locatonTest);
    }
    @Test
    void getHolidaysBetweenDates() {
        Date startDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(countryRepository.findByCode("ECU")).thenReturn(new Country());
        when(holidayRepository.findByHolidayDateBetweenAndCountry(startDate, endDate, new Country())).thenReturn(holidayList);

        List<HolidayRS> result = holidayService.getHolidaysBetweenDates(startDate, endDate, "ECU");
        assertEquals(2, result.size());
        assertEquals("Día de la Independencia", result.get(0).getName());
        assertEquals("Día de Fiesta", result.get(1).getName());
    }

    @Test
    void obtainHoliday() {
        when(holidayRepository.findByUniqueId("e65f0753-fb67-4e1e-8e5d-ca3fdcf81ee8")).thenReturn(holidayTest1);
        HolidayRS result = holidayService.obtainHoliday("e65f0753-fb67-4e1e-8e5d-ca3fdcf81ee8");
        assertNotNull(result);
        assertEquals("Día de la Independencia", result.getName());
    }

    @Test
    void obtainHolidayException() {
        when(holidayRepository.findByUniqueId("e65f0753-fb67-4e1e-8e5d-ca3fdcf81ee8")).thenReturn(null);

        assertThrows(RuntimeException.class,()->{
            HolidayRS result = holidayService.obtainHoliday("e65f0753-fb67-4e1e-8e5d-ca3fdcf81ee8");
            assertNull(result);
        });

    }

    @Test
    void createHoliday() {
        // Simulación de datos y comportamiento del repositorio

        when(countryRepository.findByCode("ECU")).thenReturn(new Country());
        when(geoLocationRepository.findById("1")).thenReturn(Optional.of(new GeoLocation()));
        when(holidayRepository.findByUniqueId(holidayRQ.getUniqueId())).thenReturn(null);

        // Llamar al servicio y realizar las aserciones
        assertDoesNotThrow(() -> holidayService.createHoliday(holidayRQ, "ECU", "1"));
    }

    @Test
    void updateHoliday() {
        given(holidayRepository.save(holidayTest1)).willReturn(holidayTest1);
        when(holidayRepository.findByUniqueId(holidayRQ.getUniqueId())).thenReturn(holidayTest1);
        holidayRQ.setName("Día de Prueba");
        // Llamar al servicio y realizar las aserciones
        HolidayRS result = holidayService.updateHoliday(holidayRQ);
        assertNotNull(result);
        assertEquals("Día de Prueba", result.getName());
    }

    @Test
    void updateHolidayException() {
        given(holidayRepository.save(holidayTest1)).willReturn(holidayTest1);
        when(holidayRepository.findByUniqueId(holidayRQ.getUniqueId())).thenReturn(null);
        assertThrows(RuntimeException.class, ()->{
            holidayService.updateHoliday(holidayRQ);
        });

        verify(holidayRepository, never()).save(any(Holiday.class));
    }

    @Test
    void logicDeleteHoliday() {
        given(holidayRepository.save(holidayTest2)).willReturn(holidayTest2);
        given(holidayRepository.findByUniqueIdAndState(holidayTest2.getUniqueId(), "ACT")).willReturn(holidayTest2);

        // Llamar al servicio y realizar las aserciones
        HolidayRS result = holidayService.logicDeleteHoliday(holidayTest2.getUniqueId());
        assertNotNull(result);
        assertEquals("INA", result.getState());
    }

    @Test
    void logicDeleteHolidayThrow() {
        given(holidayRepository.save(holidayTest2)).willReturn(holidayTest2);
        when(holidayRepository.findByUniqueIdAndState(holidayTest2.getUniqueId(), "ACT")).thenReturn(null);

        assertThrows(RuntimeException.class, ()->{
            HolidayRS result = holidayService.logicDeleteHoliday(holidayTest2.getUniqueId());
            assertNull(result);
        });

        verify(holidayRepository,never()).save(any(Holiday.class));
    }

    @Test
    void logicActivateHoliday() {
        given(holidayRepository.save(holidayTest1)).willReturn(holidayTest1);
        when(holidayRepository.findByUniqueIdAndState(holidayTest1.getUniqueId(), "INA"))
                .thenReturn(holidayTest1);
        assertDoesNotThrow(() -> {
            HolidayRS result = holidayService.logicActivateHoliday(holidayTest1.getUniqueId());
            assertNotNull(result);
            assertEquals("ACT", result.getState());
        });
    }

    @Test
    void generateHolidayWeekends() {
        // Simulación de datos y comportamiento del repositorio
        given(geoLocationRepository.save(location)).willReturn(location);
        given(countryRepository.save(country)).willReturn(country);
        given(geoStructureRepository.save(structure)).willReturn(structure);
        when(geoLocationRepository.findById(location.getId())).thenReturn(Optional.of(location));
        when(countryRepository.findByCode(country.getCode())).thenReturn(country);
        when(holidayRepository.saveAll(new ArrayList<>())).thenReturn(holidayList);

        // Llamar al servicio y realizar las aserciones
        List<HolidayRS> result = holidayService.generateHolidayWeekends(2023, 8, true, true, "ECU", "1");
        assertNotNull(result);
    }
}