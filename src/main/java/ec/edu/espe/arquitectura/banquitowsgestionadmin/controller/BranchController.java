package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Branch;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.BranchService;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/v1/branch")
public class BranchController {

    private final BranchService service;

    public BranchController(BranchService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity createBranch(@RequestBody Branch branch){
        this.service.createBranch(branch);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Branch>> listByName(@PathParam("name") String name) {
        List<Branch> branchs = this.service.listByName(name);
        return ResponseEntity.ok(branchs);
    }
    
}
