package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.BankEntity;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.BankEntityService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/bankEntity")
public class BankEntityController {

    private final BankEntityService service;

    
    public BankEntityController(BankEntityService service){
        this.service = service;
    }

    @GetMapping("/{id}/{internationalCode}")
    public ResponseEntity<BankEntity> findByPK(@PathVariable("id") String id, @PathVariable("internationalCode") String internationalCode){
        BankEntity rs = this.service.obtain(id, internationalCode);
        return ResponseEntity.ok(rs);
    }
    
}
