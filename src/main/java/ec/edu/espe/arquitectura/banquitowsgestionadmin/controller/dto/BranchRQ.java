package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchRQ {
    private String code;
    private String name;    
    private String emailAddress;
    private String phoneNumber;
    
}
