package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoLocationRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.GeoLocationRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.GeoLocationService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/geo-location")
public class GeoLocationController {
    private final GeoLocationService geoLocationService;

    public GeoLocationController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GeoLocationRQ geoLocation){
        this.geoLocationService.createGeoLocation(geoLocation);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<GeoLocationRS>> getGeoLocationByZipCode(@PathParam("zipCode") String zipCode){
        List<GeoLocationRS> rs = this.geoLocationService.findLocationByZipCode(zipCode);
        return ResponseEntity.ok(rs);
    }
}
