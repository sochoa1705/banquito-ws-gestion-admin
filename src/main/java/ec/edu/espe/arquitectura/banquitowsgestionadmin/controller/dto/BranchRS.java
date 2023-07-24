package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchRS {
    private String locationId;
    private String code;
    private String name;
    private String uniqueKey;
    private String state;
    private Date creationDate;
    private String emailAddress;
    private String phoneNumber;
    private String line1;
    private String line2;
    private Float latitude;
    private Float longitude;
}
