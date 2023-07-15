package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/holiday-list")
    public ResponseEntity<List<Holiday>> getAllHolidays() {
        return ResponseEntity.ok(holidayService.getAllHolidays());
    }

    @GetMapping("/holiday-get/{holidayId}")
    public ResponseEntity<Holiday> obtainHoliday(@PathVariable(name = "holidayId") String holidayId) {
        return ResponseEntity.ok(holidayService.obtainHoliday(holidayId));
    }

    @PostMapping("/holiday-create")
    public ResponseEntity<Holiday> createHoliday(@RequestBody Holiday holiday){
        return ResponseEntity.ok(holidayService.createHoliday(holiday));
    }


}
