package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.CountryRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.CountryRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/country")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    public ResponseEntity<?> createCountry(@RequestBody CountryRQ countryRQ) {
        this.countryService.createCountry(countryRQ);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CountryRS>> getAll(){
        List<CountryRS> countries = this.countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }
    @GetMapping("/{countryCode}")
    public ResponseEntity<CountryRS> obtain(@PathVariable String countryCode) {
        CountryRS rs = this.countryService.obtain(countryCode);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("/countries/{countryName}")
    public ResponseEntity<CountryRS> obtainByName(@PathVariable String countryName) {
        CountryRS rs = this.countryService.obtainByName(countryName);
        return ResponseEntity.ok(rs);
    }


    @PutMapping
    public ResponseEntity<CountryRS> updateCountry(@RequestBody CountryRQ country) {
        try{
            CountryRS rs = this.countryService.updateCountry(country);
            return ResponseEntity.ok(rs);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CountryRS> delete(@PathVariable String id) {
        try{
            CountryRS rs = this.countryService.logicDelete(id);
            return ResponseEntity.ok().body(rs);
        }catch (RuntimeException rte){
            throw new RuntimeException("Country not found and not can't be deleted: " + id, rte);
        }
    }

}
