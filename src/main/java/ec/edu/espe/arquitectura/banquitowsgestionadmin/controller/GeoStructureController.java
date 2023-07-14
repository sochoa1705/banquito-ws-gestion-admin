package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoLocationRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoStructureRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.GeoStructureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class GeoStructureController {
    private GeoStructureService geoStructureService;

    public GeoStructureController(GeoStructureService geoStructureService) {
        this.geoStructureService = geoStructureService;
    }

    @PostMapping
    public ResponseEntity<?> createGeoStructure(@RequestBody GeoStructureRQ geoStructure){
        this.geoStructureService.createGeoStructure(geoStructure);
        return ResponseEntity.ok().body("GeoStructure created");
    }

    @PostMapping("/locations")
    public ResponseEntity<GeoStructureRS> getStructureByLocation(@RequestBody GeoLocationRQ geoLocationRQ){
        GeoStructureRS rs = this.geoStructureService.findLocationParent(geoLocationRQ);
        return ResponseEntity.ok(rs);
    }

}
