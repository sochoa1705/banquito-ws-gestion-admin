package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class BranchRQ {
    private String locationId;
    private String code;
    private String name;
    private String emailAddress;
    private String phoneNumber;
    private String line1;
    private String line2;
    private Float latitude;
    private Float longitude;
}
