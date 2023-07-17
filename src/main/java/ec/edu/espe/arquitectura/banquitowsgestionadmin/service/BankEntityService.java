package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Branch;
import org.springframework.stereotype.Service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.BankEntity;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.BankEntityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BankEntityService {

    private final BankEntityRepository bankEntityRepository;
    
    public BankEntityService(BankEntityRepository bankEntityRepository){
        this.bankEntityRepository = bankEntityRepository;
    }

    public BankEntity obtain(String id, String internationalCode){
        return this.bankEntityRepository.findBankEntityByIdAndInternationalCode(id, internationalCode);
    }

    public BankEntity addBranch(String bankEntityId, Branch branch){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);

        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();
            branchList.add(branch);
            bankEntity.setBranches(branchList);
            return bankEntityRepository.save(bankEntity);
        }
        return null;
    }

    public List<Branch> listAllBranchesByEntity(String bankEntityId){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);

        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();
            return branchList;
        }
        return null;
    }

    public Branch BranchByCode(String bankEntityId, String branchUniqueKey){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);

        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();

            for (Branch branch : branchList) {
                if (branch.getUniqueKey().equals(branchUniqueKey)) {
                    return branch;
                }
            }
        }
        return null;
    }

    public BankEntity updateBranch(String bankEntityId, String branchUniqueKey, Branch branchBody){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);

        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();

            for (Branch branch : branchList) {
                if (branch.getUniqueKey().equals(branchUniqueKey)) {
                    branch.setName(branchBody.getName());
                    branch.setEmailAddress(branchBody.getEmailAddress());
                    branch.setPhoneNumber(branchBody.getPhoneNumber());
                }
            }
            bankEntity.setBranches(branchList);
            return bankEntityRepository.save(bankEntity);
        }
        return null;
    }

    public BankEntity deleteBranch(String bankEntityId, String branchUniqueKey, Branch branchBody){
        Optional<BankEntity> optionalBankEntity = bankEntityRepository.findById(bankEntityId);

        if (optionalBankEntity.isPresent()) {
            BankEntity bankEntity = optionalBankEntity.get();
            List<Branch> branchList = bankEntity.getBranches();

            for (Branch branch : branchList) {
                if (branch.getUniqueKey().equals(branchUniqueKey)) {
                    branch.setState(branchBody.getState());
                }
            }
            bankEntity.setBranches(branchList);
            return bankEntityRepository.save(bankEntity);
        }
        return null;
    }

}
