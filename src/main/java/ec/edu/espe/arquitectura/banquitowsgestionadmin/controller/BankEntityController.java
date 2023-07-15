package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.BankEntity;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.BankEntityService;

@RestController
@RequestMapping("/api/v1/bankEntity")

public class BankEntityController {

    private final BankEntityService service;

    
    public BankEntityController(BankEntityService service){
        this.service = service;
    }

    @GetMapping("/{id}/{internationalCode}")
    public ResponseEntity<BankEntity> findByPk(@PathVariable("id") String id, @PathVariable("internationalCode") String internationalCode){
        return ResponseEntity.ok(this.service.obtain(id, internationalCode));
    }
    
}
