package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Branch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.BankEntity;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.BankEntityService;

import java.util.List;
import java.util.Optional;

@RestController
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

    @GetMapping("/branch-list/{bankEntityId}")
    public ResponseEntity<List<Branch>> findBranchesByBankEntityId(@PathVariable("bankEntityId") String id){
        List<Branch> rs = this.service.listAllBranchesByEntity(id);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("/branch-unique/{bankEntityId}/{branchId}")
    public ResponseEntity <Branch> findBranchByCode(@PathVariable("bankEntityId") String bankEntityId,
                                                    @PathVariable("branchId") String branchId){
        Branch rs = this.service.BranchByCode(bankEntityId, branchId);
        return ResponseEntity.ok(rs);
    }

    @PutMapping("/branch-create/{bankEntityId}")
    public ResponseEntity<BankEntity> addBranch(@PathVariable String bankEntityId,
                                                @RequestBody Branch branch){
        try {
            BankEntity bankEntity = this.service.addBranch(bankEntityId, branch);
            return ResponseEntity.ok(bankEntity);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/branch-update/{bankEntityId}/{branchId}")
    public ResponseEntity<BankEntity> updateBranch(@PathVariable String bankEntityId,
                                                @PathVariable String branchId,
                                                @RequestBody Branch branch){
        try {
            BankEntity bankEntity = this.service.updateBranch(bankEntityId, branchId, branch);
            return ResponseEntity.ok(bankEntity);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/branch-delete/{bankEntityId}/{branchId}")
    public ResponseEntity<BankEntity> deleteBranch(@PathVariable String bankEntityId,
                                                   @PathVariable String branchId,
                                                   @RequestBody Branch branch){
        try {
            BankEntity bankEntity = this.service.deleteBranch(bankEntityId, branchId, branch);
            return ResponseEntity.ok(bankEntity);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

}
