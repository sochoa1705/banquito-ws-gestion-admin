package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryAndBranchCode {
    private String countryCode;
    private String branchCode;
}

