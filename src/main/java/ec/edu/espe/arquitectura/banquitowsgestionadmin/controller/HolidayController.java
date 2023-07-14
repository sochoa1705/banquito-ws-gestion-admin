package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Holiday;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.HolidayService;

@RestController
@RequestMapping("/api/v1/holiday")
public class HolidayController {
    
    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @PostMapping
    public ResponseEntity createHoliday(@RequestBody HolidayRQ holiday){
        this.holidayService.createHoliday(holiday);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{name}")
    public ResponseEntity<List<Holiday>> listByName(@PathVariable("name") String name){
        List<Holiday> holidays = this.holidayService.listByName(name);
        return ResponseEntity.ok(holidays);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<Holiday>> listByType(@PathVariable("type") String type){
        List<Holiday> holidays = this.holidayService.listByType(type);
        return ResponseEntity.ok(holidays);
    }
}
