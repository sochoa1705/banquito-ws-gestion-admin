package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HolidayRQ {  
    private Date holidayDate;
    @DBRef
    private String location; 
    @DBRef
    private String country; 
    private String name;
    private String type;
}
