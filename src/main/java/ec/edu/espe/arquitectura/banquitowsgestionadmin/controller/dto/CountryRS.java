package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryRS {
    private String code;
    private String name;
    private String phoneCode;
}
