package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.GeoStructureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/geo-structure")
public class GeoStructureController {
    private final GeoStructureService geoStructureService;

    public GeoStructureController(GeoStructureService geoStructureService) {
        this.geoStructureService = geoStructureService;
    }

    @PostMapping
    public ResponseEntity<?> createGeoStructure(@RequestBody GeoStructureRQ geoStructure, @RequestParam String countryCode) {
        this.geoStructureService.createGeoStructure(geoStructure, countryCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/provinces/{countryCode}")
    public ResponseEntity<GeoStructureRS> obtainProvincesFromCountry(@RequestParam Integer levelCode, @PathVariable String countryCode) {
        GeoStructureRS geoStructureRS = this.geoStructureService.obtainStructureFromCountry(levelCode, countryCode);
        return ResponseEntity.ok().body(geoStructureRS);
    }

}
