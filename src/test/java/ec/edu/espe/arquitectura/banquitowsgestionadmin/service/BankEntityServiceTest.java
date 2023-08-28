package ec.edu.espe.arquitectura.banquitowsgestionadmin.service;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.*;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.BankEntityRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoLocationRepository;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.repository.GeoStructureRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.jupiter.api.Assertions.*;

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

    @Mock
    private GeoLocationRepository geoLocationRepository;

    @Mock
    private GeoStructureRepository geoStructureRepository;

    @InjectMocks
    private GeoLocationService geoLocationService;

    @InjectMocks
    private BankEntityService bankEntityService;

    @InjectMocks
    private GeoStructureService geoStructureService;

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

    @Test
    public void testGetGeoLocationNameByBranchUniqueKey() {
        // Mocking data
        BankEntity bankEntity = new BankEntity();
        List<Branch> branchList = new ArrayList<>();
        Branch branch = new Branch();
        branch.setUniqueKey("uniqueKey");
        branch.setLocationId("locationId");
        branchList.add(branch);
        bankEntity.setBranches(branchList);

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setId("locationId");
        geoLocation.setName("Test Location");

        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(bankEntity));
        Mockito.when(geoLocationRepository.findById(Mockito.anyString())).thenReturn(Optional.of(geoLocation));

        // Calling the service method
        String result = bankEntityService.getGeoLocationNameByBranchUniqueKey("bankEntityId", "uniqueKey");

        // Asserting the result
        assertEquals("Test Location", result);
    }

    @Test
    public void testGetGeoLocationNameByBranchUniqueKeyBranchNotFound() {
        // Mocking data
        BankEntity bankEntity = new BankEntity();
        List<Branch> branchList = new ArrayList<>();
        bankEntity.setBranches(branchList);

        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(bankEntity));

        // Calling the service method
        String result = bankEntityService.getGeoLocationNameByBranchUniqueKey("bankEntityId", "uniqueKey");

        // Asserting the result
        assertNull(result);
    }

    @Test
    public void testGetGeoLocationNameByBranchUniqueKeyGeoLocationNotFound() {
        // Mocking data
        BankEntity bankEntity = new BankEntity();
        List<Branch> branchList = new ArrayList<>();
        Branch branch = new Branch();
        branch.setUniqueKey("uniqueKey");
        branch.setLocationId("locationId");
        branchList.add(branch);
        bankEntity.setBranches(branchList);

        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(bankEntity));
        Mockito.when(geoLocationRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        // Calling the service method
        String result = bankEntityService.getGeoLocationNameByBranchUniqueKey("bankEntityId", "uniqueKey");

        // Asserting the result
        assertEquals("GeoLocalización desconocida", result);
    }

    @Test
    public void testGetCountryAndBranchCodeByBranchUniqueKey() {
        // Mocking data
        BankEntity bankEntity = new BankEntity();
        List<Branch> branchList = new ArrayList<>();
        Branch branch = new Branch();
        branch.setUniqueKey("uniqueKey");
        branch.setLocationId("locationId");
        branch.setCodeNumber("001");
        branchList.add(branch);
        bankEntity.setBranches(branchList);

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setId("locationId");

        GeoStructure geoStructure = new GeoStructure();
        Country country = new Country();
        country.setCode("EC");
        geoStructure.setCountry(country);

        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(bankEntity));
        Mockito.when(geoLocationRepository.findById(Mockito.anyString())).thenReturn(Optional.of(geoLocation));
        Mockito.when(geoStructureRepository.findByLocations_Id(Mockito.anyString())).thenReturn(geoStructure);

        // Calling the service method
        CountryAndBranchCode result = bankEntityService.getCountryAndBranchCodeByBranchUniqueKey("uniqueKey");

        // Asserting the result
        assertNotNull(result);
        assertEquals("EC", result.getCountryCode());
        assertEquals("001", result.getBranchCode());
    }

    @Test
    public void testListAllBranchesByEntity() {
        // Mocking data
        BankEntity bankEntity = new BankEntity();
        List<Branch> branchList = new ArrayList<>();
        Branch branch1 = new Branch();
        branch1.setUniqueKey("uniqueKey1");
        branch1.setLocationId("locationId1");
        branchList.add(branch1);
        Branch branch2 = new Branch();
        branch2.setUniqueKey("uniqueKey2");
        branch2.setLocationId("locationId2");
        branchList.add(branch2);
        bankEntity.setBranches(branchList);

        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(bankEntity));

        // Calling the service method
        List<BranchRS> result = bankEntityService.listAllBranchesByEntity("bankEntityId");

        // Asserting the result
        assertNotNull(result);
        assertEquals(2, result.size());

        // You can add more assertions for individual branch details in the result list if needed
        // For example:
        assertEquals("uniqueKey1", result.get(0).getUniqueKey());
        assertEquals("locationId1", result.get(0).getLocationId());
        // ... and so on
    }

    @Test
    public void testListAllBranchesByEntityEntityNotFound() {
        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        // Calling the service method
        List<BranchRS> result = bankEntityService.listAllBranchesByEntity("nonExistentEntityId");

        // Asserting the result
        assertNull(result);
    }

    @Test
    public void testBranchByCode() {
        // Mocking data
        BankEntity bankEntity = new BankEntity();
        List<Branch> branchList = new ArrayList<>();
        Branch branch1 = new Branch();
        branch1.setUniqueKey("uniqueKey1");
        branch1.setLocationId("locationId1");
        branchList.add(branch1);
        Branch branch2 = new Branch();
        branch2.setUniqueKey("uniqueKey2");
        branch2.setLocationId("locationId2");
        branchList.add(branch2);
        bankEntity.setBranches(branchList);

        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(bankEntity));

        // Calling the service method
        BranchRS result = bankEntityService.BranchByCode("bankEntityId", "uniqueKey2");

        // Asserting the result
        assertNotNull(result);
        assertEquals("uniqueKey2", result.getUniqueKey());
        assertEquals("locationId2", result.getLocationId());
        // ... and so on
    }

    @Test
    public void testBranchByCodeEntityNotFound() {
        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        // Calling the service method
        BranchRS result = bankEntityService.BranchByCode("nonExistentEntityId", "uniqueKey");

        // Asserting the result
        assertNull(result);
    }

    @Test
    public void testBranchByCodeBranchNotFound() {
        // Mocking data
        BankEntity bankEntity = new BankEntity();
        List<Branch> branchList = new ArrayList<>();
        bankEntity.setBranches(branchList);

        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(bankEntity));

        // Calling the service method
        BranchRS result = bankEntityService.BranchByCode("bankEntityId", "nonExistentUniqueKey");

        // Asserting the result
        assertNull(result);
    }

    @Test
    public void testUpdateBranchExistingBranch() {
        // Mocking data
        BankEntity bankEntity = new BankEntity();
        List<Branch> branchList = new ArrayList<>();
        Branch branch = new Branch();
        branch.setUniqueKey("uniqueKey");
        branchList.add(branch);
        bankEntity.setBranches(branchList);

        BranchRQ updatedBranchRQ = new BranchRQ();
        updatedBranchRQ.setName("Updated Name");
        updatedBranchRQ.setEmailAddress("updated@example.com");
        updatedBranchRQ.setPhoneNumber("123456789");

        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(bankEntity));
        Mockito.when(bankEntityRepository.save(Mockito.any())).thenReturn(bankEntity);

        // Calling the service method
        BankEntity result = bankEntityService.updateBranch("bankEntityId", "uniqueKey", updatedBranchRQ);

        // Asserting the result
        assertNotNull(result);
        assertEquals("Updated Name", result.getBranches().get(0).getName());
        assertEquals("updated@example.com", result.getBranches().get(0).getEmailAddress());
        assertEquals("123456789", result.getBranches().get(0).getPhoneNumber());
    }

    @Test
    public void testUpdateBranchNewBranch() {
        // Mocking data
        BankEntity bankEntity = new BankEntity();
        List<Branch> branchList = new ArrayList<>();
        bankEntity.setBranches(branchList);

        BranchRQ newBranchRQ = new BranchRQ();
        newBranchRQ.setName("New Branch");
        newBranchRQ.setEmailAddress("new@example.com");
        newBranchRQ.setPhoneNumber("987654321");

        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.of(bankEntity));
        Mockito.when(bankEntityRepository.save(Mockito.any())).thenReturn(bankEntity);

        // Calling the service method
        BankEntity result = bankEntityService.updateBranch("bankEntityId", "nonExistentUniqueKey", newBranchRQ);

        // Asserting the result
        assertNotNull(result);
        assertEquals(1, result.getBranches().size());
        assertEquals("New Branch", result.getBranches().get(0).getName());
        assertEquals("new@example.com", result.getBranches().get(0).getEmailAddress());
        assertEquals("987654321", result.getBranches().get(0).getPhoneNumber());
    }

    @Test
    public void testUpdateBranchBankEntityNotFound() {
        // Mocking repository responses
        Mockito.when(bankEntityRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        // Calling the service method
        BankEntity result = bankEntityService.updateBranch("nonExistentEntityId", "uniqueKey", new BranchRQ());

        // Asserting the result
        assertNull(result);
    }

}
