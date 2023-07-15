package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.HolidayService;

@RestController
@RequestMapping("/api/v1/holiday")
public class HolidayController {
    
    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping("/holidays")
    public ResponseEntity<List<Holiday>> getAllHolidays() {
        List<Holiday> holidays = holidayService.getAllHolidays();
        return new ResponseEntity<>(holidays, HttpStatus.OK);
    }

    @GetMapping("/holidays-location")
    public List<Holiday> getAllHolidaysWithLocation() {
        return holidayService.getAllHolidaysWithLocation();
    }
}
