package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeoLocationRS {
    private String name;
    private String areaPhoneCode;
    private String zipCode;
    private String locationParent;
}
