package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRQ;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto.BranchRS;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.*;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.service.BankEntityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.equalTo;

@WebMvcTest(BankEntityController.class)
class BankEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankEntityService bankEntityService;

    private final static  String BASE_URL = "/api/v1/bankEntity";

    @Test
    public void testFindByPK() throws Exception {
        // Mock the behavior of bankEntityService.obtain() method
        BankEntity mockBankEntity = new BankEntity(); // Create a mock BankEntity object
        when(bankEntityService.obtain(anyString(), anyString())).thenReturn(mockBankEntity);

        // Perform the GET request
        mockMvc.perform(get("/api/v1/bankEntity/{id}/{internationalCode}", "64b1892b9c2c3b03c33a736f", "BQUIEC01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // Add more assertions as needed
    }

    @Test
    public void testFindBranchesByBankEntityId() throws Exception {
        // Create a mock list of BranchRS objects
        List<BranchRS> mockBranches = new ArrayList<>();
        mockBranches.add(new BranchRS());
        mockBranches.add(new BranchRS());

        // Mock the behavior of bankEntityService.listAllBranchesByEntity() method
        when(bankEntityService.listAllBranchesByEntity(anyString())).thenReturn(mockBranches);

        // Perform the GET request
        mockMvc.perform(get("/api/v1/bankEntity/branch-list/{bankEntityId}", "bankEntityIdValue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2)); // Assuming there are 2 branches in the mockBranches list

        // Verify that bankEntityService.listAllBranchesByEntity() was called with the correct parameter
        verify(bankEntityService, times(1)).listAllBranchesByEntity("bankEntityIdValue");
    }

    @Test
    public void testGetCountryAndBranchCode() throws Exception {
        // Create a mock CountryAndBranchCode object
        CountryAndBranchCode mockCountryAndBranchCode = new CountryAndBranchCode("EC", "001");

        // Mock the behavior of bankEntityService.getCountryAndBranchCodeByBranchUniqueKey() method
        when(bankEntityService.getCountryAndBranchCodeByBranchUniqueKey(anyString())).thenReturn(mockCountryAndBranchCode);

        // Perform the GET request
        mockMvc.perform(get("/api/v1/bankEntity/branch-geoStructure/{branchUniqueKey}", "branchUniqueKeyValue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.countryCode", equalTo("EC"))) // Adjust this to match the expected JSON response
                .andExpect(jsonPath("$.branchCode", equalTo("001"))); // Adjust this to match the expected JSON response

        // Verify that bankEntityService.getCountryAndBranchCodeByBranchUniqueKey() was called with the correct parameter
        verify(bankEntityService, times(1)).getCountryAndBranchCodeByBranchUniqueKey("branchUniqueKeyValue");
    }

    @Test
    public void testFindBranchByCode() throws Exception {
        // Create a mock BranchRS object using the builder
        BranchRS mockBranchRS = BranchRS.builder()
                .name("BanQuito New Update")
                .code("BQUIEC17SL")
                .locationId("64b0a7be466dc541f29fdb8e")
                .build();

        // Mock the behavior of bankEntityService.BranchByCode() method
        when(bankEntityService.BranchByCode(anyString(), anyString())).thenReturn(mockBranchRS);

        // Perform the GET request
        mockMvc.perform(get("/api/v1/bankEntity/branch-unique/{bankEntityId}/{branchId}", "64b1892b9c2c3b03c33a736f", "c81b6e7a-9a9f-4d2e-8a51-7fdca2ef1f29")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", equalTo("BanQuito New Update")))
                .andExpect(jsonPath("$.code", equalTo("BQUIEC17SL")))
                .andExpect(jsonPath("$.locationId", equalTo("64b0a7be466dc541f29fdb8e")));

        // Verify that bankEntityService.BranchByCode() was called with the correct parameters
        verify(bankEntityService, times(1)).BranchByCode("64b1892b9c2c3b03c33a736f", "c81b6e7a-9a9f-4d2e-8a51-7fdca2ef1f29");
    }

    @Test
    public void testUpdateBranch() throws Exception {
        // Create a mock BankEntity object
        BankEntity mockBankEntity = new BankEntity();
        // Set up the behavior of bankEntityService.updateBranch() method
        when(bankEntityService.updateBranch(anyString(), anyString(), any(BranchRQ.class))).thenReturn(mockBankEntity);

        // Create a BranchRQ object with appropriate values
        BranchRQ branchRQ = BranchRQ.builder()
                .locationId("64b0a7be466dc541f29fdb8e")
                .name("Updated Branch")
                .emailAddress("updated@test.com")
                .phoneNumber("1234567")
                // Set other fields as needed
                .build();

        // Perform the PUT request
        mockMvc.perform(put("/api/v1/bankEntity/branch-update/{bankEntityId}/{branchId}", "64b1892b9c2c3b03c33a736f", "c81b6e7a-9a9f-4d2e-8a51-7fdca2ef1f29")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(branchRQ))) // Convert object to JSON string
                .andExpect(status().isOk());

        // Verify that bankEntityService.updateBranch() was called with the correct parameters
        verify(bankEntityService, times(1)).updateBranch(eq("64b1892b9c2c3b03c33a736f"), eq("c81b6e7a-9a9f-4d2e-8a51-7fdca2ef1f29"), eq(branchRQ));
    }

    // Helper method to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeleteBranch() throws Exception {
        // Create a mock BranchRS object using the builder
        BranchRS mockBranchRS = BranchRS.builder()
                .name("Deleted Branch")
                .code("DELBRI01")
                .locationId("64b0a7be466dc541f29fdb8e")
                .build();

        // Mock the behavior of bankEntityService.deleteBranch() method
        when(bankEntityService.deleteBranch(anyString(), anyString())).thenReturn(mockBranchRS);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/v1/bankEntity/branch-delete/{bankEntityId}/{branchId}", "64b1892b9c2c3b03c33a736f", "c81b6e7a-9a9f-4d2e-8a51-7fdca2ef1f29")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", equalTo("Deleted Branch")))
                .andExpect(jsonPath("$.code", equalTo("DELBRI01")))
                .andExpect(jsonPath("$.locationId", equalTo("64b0a7be466dc541f29fdb8e")));

        // Verify that bankEntityService.deleteBranch() was called with the correct parameters
        verify(bankEntityService, times(1)).deleteBranch("64b1892b9c2c3b03c33a736f", "c81b6e7a-9a9f-4d2e-8a51-7fdca2ef1f29");
    }

}
