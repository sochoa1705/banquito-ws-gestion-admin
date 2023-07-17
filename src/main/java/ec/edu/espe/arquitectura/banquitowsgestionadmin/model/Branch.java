package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import java.util.Date;
import lombok.Data;
import lombok.Builder;


@Data
@Builder
public class Branch {

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

    private String status;



}
