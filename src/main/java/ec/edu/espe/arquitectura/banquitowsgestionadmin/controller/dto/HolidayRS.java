package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HolidayRS {
    private Date holidayDate;
    private String location; 
    private String country; 
    private String name;
    private String type;
}
