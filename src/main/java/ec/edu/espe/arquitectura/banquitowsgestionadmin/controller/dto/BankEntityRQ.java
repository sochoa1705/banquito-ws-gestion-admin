package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Branch;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BankEntityRQ {
    private String name;
    private String internationalCode;
    private List<Branch> branches;
}
