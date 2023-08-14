package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.BankEntity;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Branch;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.BankEntityRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BankEntityServiceTest {
    @Mock
    private BankEntityRepository bankEntityRepository;

    @InjectMocks
    private BankEntityService bankEntityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void BankEntityService_AddBranch_Success(){
        String bankEntityId = "1";
        BranchRQ branch = BranchRQ.builder()
                .locationId("1")
                .code("1")
                .name("Sucursal 1")
                .emailAddress("br@banquito.com")
                .phoneNumber("0999999999")
                .line1("Av. 6 de Diciembre")
                .line2("N34-451")
                .latitude(0.0f)
                .longitude(0.0f)
                .build();

        BankEntity bankEntity = new BankEntity();
        List<Branch> branchList = new ArrayList<>();
        branchList.add( bankEntityService.transformBranchRQ(branch));
        bankEntity.setId(bankEntityId);
        bankEntity.setName("Banquito");
        bankEntity.setInternationalCode("BQUIEC01");
        bankEntity.setBranches(branchList);


        when(bankEntityRepository.findById(bankEntityId)).thenReturn(Optional.of(bankEntity));
        when(bankEntityRepository.save(any())).thenReturn(bankEntity);

        BankEntity result = bankEntityService.addBranch(bankEntityId, branch);

        verify(bankEntityRepository).findById(bankEntityId);
        verify(bankEntityRepository).save(any());


    }

}
