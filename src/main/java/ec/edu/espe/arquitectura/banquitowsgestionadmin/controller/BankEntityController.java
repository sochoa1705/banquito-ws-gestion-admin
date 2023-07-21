package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRS;
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
    public ResponseEntity<List<BranchRS>> findBranchesByBankEntityId(@PathVariable("bankEntityId") String id){
        List<BranchRS> rs = this.service.listAllBranchesByEntity(id);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("/branch-unique/{bankEntityId}/{branchId}")
    public ResponseEntity <BranchRS> findBranchByCode(@PathVariable("bankEntityId") String bankEntityId,
                                                    @PathVariable("branchId") String branchId){
        BranchRS rs = this.service.BranchByCode(bankEntityId, branchId);
        return ResponseEntity.ok(rs);
    }

    @PutMapping("/branch-create/{bankEntityId}")
    public ResponseEntity<BankEntity> addBranch(@PathVariable String bankEntityId,
                                                @RequestBody BranchRQ branchRQ){
        try {
            BankEntity bankEntity = this.service.addBranch(bankEntityId, branchRQ);
            return ResponseEntity.ok(bankEntity);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/branch-update/{bankEntityId}/{branchId}")
    public ResponseEntity<BankEntity> updateBranch(@PathVariable String bankEntityId,
                                                @PathVariable String branchId,
                                                @RequestBody BranchRQ branchRQ){
        try {
            BankEntity bankEntity = this.service.updateBranch(bankEntityId, branchId, branchRQ);
            return ResponseEntity.ok(bankEntity);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/branch-delete/{bankEntityId}/{branchId}")
    public ResponseEntity<BranchRS> deleteBranch(@PathVariable String bankEntityId,
                                                   @PathVariable String branchId
                                                   ){
        try {
            BranchRS rs = this.service.deleteBranch(bankEntityId, branchId);
            return ResponseEntity.ok(rs);
        }catch (RuntimeException rte){
            return ResponseEntity.badRequest().build();
        }
    }

}
