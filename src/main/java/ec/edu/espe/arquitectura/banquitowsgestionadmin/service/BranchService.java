package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import java.util.List;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Branch;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.BranchRepository;

public class BranchService {
    
    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }
    
    public void createBranch(Branch branch) {
        this.branchRepository.save(branch);
    }

    public List<Branch> listByName(String name){
        return this.branchRepository.findbyName(name);
    }
}
