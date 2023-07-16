package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import java.util.List;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRS;
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

    @GetMapping("/holiday-list/{type}")
    public ResponseEntity<List<HolidayRS>> getHolidaysByType(@PathVariable(name = "type") String type) {
        List<HolidayRS> holidays = this.holidayService.getAllHolidays(type);
        return ResponseEntity.ok(holidays);
    }

    @GetMapping("/holiday-get/{holidayId}")
    public ResponseEntity<HolidayRS> obtainHoliday(@PathVariable(name = "holidayId") String holidayId) {
        HolidayRS holidays = this.holidayService.obtainHoliday(holidayId);
        return ResponseEntity.ok(holidays);
    }

    @PostMapping("/holiday-create")
    public ResponseEntity<?> createHoliday(@RequestBody HolidayRQ holidayRQ){
        return ResponseEntity.ok(holidayService.createHoliday(holidayRQ));
    }

    @PostMapping("/holiday-generate/{year}/{month}/{saturday}/{sunday}/{codeCountry}/{idLocation}")
    public ResponseEntity<List<HolidayRS>> generateHolidays(@PathVariable(name="year") Integer year,
                                                          @PathVariable(name="month") Integer month,
                                                          @PathVariable(name="saturday") Boolean saturday,
                                                          @PathVariable(name="sunday") Boolean sunday,
                                                          @PathVariable(name="codeCountry") String codeCountry,
                                                          @PathVariable(name="idLocation") String idLocation){

        try {
            List<HolidayRS> holidayRSList = holidayService.generateHolidayWeekends(year,
                    month, saturday, sunday, codeCountry, idLocation);
            return ResponseEntity.ok(holidayRSList);
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/holiday-update")
    public ResponseEntity<HolidayRS> updateHoliday(@RequestBody HolidayRQ holidayRQ){
        try{
            HolidayRS rs = this.holidayService.updateHoliday(holidayRQ);
            return ResponseEntity.ok(rs);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/holiday-delete/{id}")
    public ResponseEntity<HolidayRS> deleteHoliday(@PathVariable String id){
        try{
            HolidayRS rs = this.holidayService.logicDeleteHoliday(id);
            return ResponseEntity.ok().body(rs);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }
}
