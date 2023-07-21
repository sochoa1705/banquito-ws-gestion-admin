package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import java.util.Date;
import java.util.List;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.HolidayRS;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.HolidayService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/holiday")
public class HolidayController {
    
    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping("/holiday-list-type/{type}")
    public ResponseEntity<List<HolidayRS>> getHolidaysByType(@PathVariable(name = "type") String type) {
        List<HolidayRS> holidays = this.holidayService.getHolidaysByType(type);
        return ResponseEntity.ok(holidays);
    }

    @GetMapping("/holiday-list-name/{name}")
    public ResponseEntity<List<HolidayRS>> getHolidaysByName(@PathVariable(name = "name") String name) {
        List<HolidayRS> holidays = this.holidayService.getHolidaysByName(name);
        return ResponseEntity.ok(holidays);
    }

    @GetMapping("/holiday-list-between-dates")
    public ResponseEntity<List<HolidayRS>> getHolidaysBetweenDates(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") Date start,
                                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") Date end) {
        List<HolidayRS> holidayList = this.holidayService.getHolidaysBetweenDates(start, end);
        return ResponseEntity.ok(holidayList);
    }

    @GetMapping("/holiday-get/{holidayId}")
    public ResponseEntity<HolidayRS> obtainHoliday(@PathVariable(name = "holidayId") String holidayId) {
        HolidayRS holidays = this.holidayService.obtainHoliday(holidayId);
        return ResponseEntity.ok(holidays);
    }

    @PostMapping("/holiday-create")
    public ResponseEntity<?> createHoliday(@RequestBody HolidayRQ holidayRQ){
        try{
            this.holidayService.createHoliday(holidayRQ);
            return ResponseEntity.ok().build();
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/holiday-generate")
    public ResponseEntity<List<HolidayRS>> generateHolidays(@RequestParam Integer year,
                                                          @RequestParam Integer month,
                                                          @RequestParam(defaultValue = "false") Boolean saturday,
                                                          @RequestParam(defaultValue = "false") Boolean sunday,
                                                          @RequestParam String codeCountry,
                                                          @RequestParam(required=false) String idLocation){

        try {
            List<HolidayRS> holidayRSList = this.holidayService.generateHolidayWeekends(year,
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
