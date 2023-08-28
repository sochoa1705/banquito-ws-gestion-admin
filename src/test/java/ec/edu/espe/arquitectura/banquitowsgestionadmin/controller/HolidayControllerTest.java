package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(HolidayController.class)
class HolidayControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HolidayService holidayService;
    @Autowired
    private ObjectMapper objectMapper;

    private final static  String BASE_URL = "/api/v1/holiday";
    private HolidayRQ holidayRQ;
    private Country country;
    private GeoLocation location;
    @BeforeEach
    void setUp() {
        location = GeoLocation.builder()
                .id("1")
                .name("Quito")
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
        holidayRQ = HolidayRQ.builder()
                .holidayDate(new Date())
                .name("Día de Prueba")
                .type("REG")
                .country(country)
                .location(location)
                .build();
    }
    @Test
    void getHolidaysBetweenDates() throws Exception {

        MvcResult  result = mockMvc.perform(get(BASE_URL+"/holiday-list-between-dates")
                .param("start", "2023-08-01T00:00:00.000Z")
                .param("end", "2023-08-31T23:59:59.999Z")
                .param("country", "ECU")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void obtainHoliday() throws Exception {

        MvcResult result = mockMvc.perform(get(BASE_URL + "/holiday-get/{holidayId}", "123")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void createHoliday() throws Exception {
        MvcResult result = mockMvc.perform(post(BASE_URL + "/holiday-create")
                        .param("codeCountry", "ECU")
                        .param("idLocation", "123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(holidayRQ)))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void createHolidayException() throws Exception {
        HolidayRQ holidayErrorRQ = new HolidayRQ();
        MvcResult result = mockMvc.perform(post(BASE_URL + "/holiday-create")
                        .param("codeCountry", "ECU")
                        .param("idLocation", "123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(holidayErrorRQ)))
                .andReturn();
        assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    void generateHolidays() throws Exception {
        when(holidayService.generateHolidayWeekends(anyInt(), anyInt(), anyBoolean(), anyBoolean(), anyString(), anyString()))
                .thenReturn(new ArrayList<HolidayRS>());
        MvcResult result = mockMvc.perform(post(BASE_URL + "/holiday-generate")
                        .param("year", "2023")
                        .param("month", "8")
                        .param("saturday", "true")
                        .param("sunday", "false")
                        .param("codeCountry", "ECU")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void generateHolidaysException() throws Exception {
        when(holidayService.generateHolidayWeekends(anyInt(), anyInt(), anyBoolean(), anyBoolean(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Error message"));
        MvcResult result = mockMvc.perform(post(BASE_URL + "/holiday-generate"))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void updateHoliday() throws Exception {
        MvcResult result = mockMvc.perform(put(BASE_URL + "/holiday-update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(holidayRQ)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void updateHolidayException() throws Exception {
        when(holidayService.updateHoliday(any(HolidayRQ.class)))
                .thenThrow(new RuntimeException());
        HolidayRQ request = new HolidayRQ();
        MvcResult result = mockMvc.perform(put(BASE_URL + "/holiday-update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
        JsonNode responseBody = objectMapper.readTree(result.getResponse().getContentAsString());
        assertTrue(responseBody.isEmpty());
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void activateHoliday() throws Exception {

        MvcResult result = mockMvc.perform(put(BASE_URL + "/holiday-activate/{id}", "123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void deleteHoliday() throws Exception {

        MvcResult result = mockMvc.perform(delete(BASE_URL + "/holiday-delete/{id}", "123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }
}