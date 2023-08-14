package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryRQ {
    private String code;
    private String name;
    private String phoneCode;
}
