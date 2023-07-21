package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Branch;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import org.springframework.stereotype.Service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.BankEntity;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.BankEntityRepository;

import java.util.*;

@Service
public class BankEntityService {

    private final BankEntityRepository bankEntityRepository;



    public BankEntityService(BankEntityRepository bankEntityRepository){
        this.bankEntityRepository = bankEntityRepository;
    }

    public BankEntity obtain(String id, String internationalCode){
        return this.bankEntityRepository.findBankEntityByIdAndInternationalCode(id, internationalCode);
    }

    public List<BranchRS> listAllBranchesByEntity(String bankEntityId){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();
            List<BranchRS> branchRSList = new ArrayList<>();
            for(Branch branch : branchList){
                branchRSList.add(this.transformBranch(branch));
            }
            return branchRSList;
        }
        return null;
    }

    public BranchRS BranchByCode(String bankEntityId, String branchUniqueKey){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();
            for (Branch branch : branchList) {
                if (branch.getUniqueKey().equals(branchUniqueKey)) {
                    return this.transformBranch(branch);
                }
            }
        }
        return null;
    }

    public BankEntity addBranch(String bankEntityId, BranchRQ branchRQ){
        try{
            Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
            if (optionalBankEntity.isPresent()) {
                BankEntity bankEntity = optionalBankEntity.get();
                List<Branch> branchList = bankEntity.getBranches();
                Branch newBranch = this.transformBranchRQ(branchRQ);
                newBranch.setState("ACT");
                newBranch.setUniqueKey(UUID.randomUUID().toString());
                newBranch.setCreationDate(Date.from(new Date().toInstant()));
                branchList.add(newBranch);
                bankEntity.setBranches(branchList);
                return bankEntityRepository.save(bankEntity);
            }
            return null;
        }catch (RuntimeException rte){
            throw new RuntimeException("Branch can't be created: ", rte);
        }
    }

    public BankEntity updateBranch(String bankEntityId, String branchUniqueKey, BranchRQ branchRQ){
        try{
            Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
            if (optionalBankEntity.isPresent()) {
                BankEntity bankEntity = optionalBankEntity.get();
                List<Branch> branchList = bankEntity.getBranches();
                Branch newBranch = this.transformBranchRQ(branchRQ);

                for (Branch branch : branchList) {
                    if (branch.getUniqueKey().equals(branchUniqueKey)) {
                        branch.setName(branchRQ.getName());
                        branch.setEmailAddress(branchRQ.getEmailAddress());
                        branch.setPhoneNumber(branchRQ.getPhoneNumber());
                    }
                }
                branchList.add(newBranch);
                bankEntity.setBranches(branchList);
                return bankEntityRepository.save(bankEntity);
            }
            return null;
        }catch (RuntimeException rte){
            throw new RuntimeException("Branch can't be created: ", rte);
        }
    }
    //NO FUNCIONA DELETE
    public BranchRS deleteBranch(String bankEntityId, String branchUniqueKey){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);
        BranchRS rs = null;
        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();
            for (Branch branch : branchList) {
                if (branch.getUniqueKey().equals(branchUniqueKey)) {
                    branch.setState("INA");
                    rs = this.transformBranch(branch);
                }
            }
            bankEntity.setBranches(branchList);
            this.bankEntityRepository.save(bankEntity);
            return rs;
        }
        return null;
    }

    public BranchRS transformBranch(Branch branch){
        BranchRS branchRS = BranchRS
                .builder()
                .locationId(branch.getLocationId())
                .code(branch.getCode())
                .name(branch.getName())
                .uniqueKey(branch.getUniqueKey())
                .state(branch.getState())
                .creationDate(branch.getCreationDate())
                .emailAddress(branch.getEmailAddress())
                .phoneNumber(branch.getPhoneNumber())
                .line1(branch.getLine1())
                .line2(branch.getLine2())
                .latitude(branch.getLatitude())
                .longitude(branch.getLongitude())
                .build();
            return branchRS;
    }


    public Branch transformBranchRQ(BranchRQ rq){
        Branch branch = Branch
                .builder()
                .locationId(rq.getLocationId())
                .code(rq.getCode())
                .name(rq.getName())
                .emailAddress(rq.getEmailAddress())
                .phoneNumber(rq.getPhoneNumber())
                .line1(rq.getLine1())
                .line2(rq.getLine2())
                .latitude(rq.getLatitude())
                .longitude(rq.getLongitude())
                .build();
        return branch;
    }

}
