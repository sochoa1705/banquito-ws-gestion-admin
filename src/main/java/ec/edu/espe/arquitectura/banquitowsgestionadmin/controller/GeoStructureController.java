package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoLocationRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.GeoStructureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/geo-structure")
public class GeoStructureController {
    private final GeoStructureService geoStructureService;

    public GeoStructureController(GeoStructureService geoStructureService) {
        this.geoStructureService = geoStructureService;
    }

    @PostMapping
    public ResponseEntity<?> createGeoStructure(@RequestBody GeoStructureRQ geoStructure, @RequestParam String countryCode){
        this.geoStructureService.createGeoStructure(geoStructure, countryCode);
        return ResponseEntity.ok().body("GeoStructure created");
    }

    @PostMapping("/locations")
    public ResponseEntity<GeoStructureRS> getStructureByLocation(@RequestBody GeoLocationRQ geoLocationRQ){
        GeoStructureRS rs = this.geoStructureService.findLocationParent(geoLocationRQ);
        return ResponseEntity.ok(rs);
    }

}
