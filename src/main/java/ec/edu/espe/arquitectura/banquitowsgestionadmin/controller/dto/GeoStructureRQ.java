package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeoStructureRQ {
    private Integer levelCode;
    private String name;
}
