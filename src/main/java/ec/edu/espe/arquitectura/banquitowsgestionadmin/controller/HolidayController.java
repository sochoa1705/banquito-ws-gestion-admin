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

    @PostMapping("/holiday-generate/{year}/{month}/{saturday}/{sunday}/{codeCountry}/{idLocation}")
    public ResponseEntity<List<Holiday>> generateHolidays(@PathVariable(name="year") Integer year,
                                                          @PathVariable(name="month") Integer month,
                                                          @PathVariable(name="saturday") Boolean saturday,
                                                          @PathVariable(name="sunday") Boolean sunday,
                                                          @PathVariable(name="codeCountry") String codeCountry,
                                                          @PathVariable(name="idLocation") String idLocation){

        try {
            return ResponseEntity.ok(holidayService.generateHolidayeekends(year,
                    month, saturday, sunday, codeCountry, idLocation));
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/holiday-update")
    public ResponseEntity<Holiday> updateHoliday(@RequestBody Holiday holiday){
        try{
            return ResponseEntity.ok(holidayService.updateHoliday(holiday));
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/holiday-delete/{id}")
    public ResponseEntity<Holiday> deleteHoliday(@PathVariable String id){
        try{
            return ResponseEntity.ok(holidayService.logicDeleteHoliday((id)));
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }
}
