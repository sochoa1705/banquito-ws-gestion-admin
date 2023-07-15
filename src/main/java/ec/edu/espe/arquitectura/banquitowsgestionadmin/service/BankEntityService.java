package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import org.springframework.stereotype.Service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.BankEntity;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.BankEntityRepository;

@Service
public class BankEntityService {

    private final BankEntityRepository bankEntityRepository;
    
    public BankEntityService(BankEntityRepository bankEntityRepository){
        this.bankEntityRepository = bankEntityRepository;
    }

    public BankEntity obtain(String id, String internationalCode){
        return this.bankEntityRepository.findByIdAndInternationalCode(id, internationalCode);
    }
    
}
