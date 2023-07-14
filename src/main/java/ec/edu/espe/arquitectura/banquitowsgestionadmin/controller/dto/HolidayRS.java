package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HolidayRS {
    private Date holidayDate;
    private String locationId; 
    private String countryId; 
    private String name;
    private String type;
}
