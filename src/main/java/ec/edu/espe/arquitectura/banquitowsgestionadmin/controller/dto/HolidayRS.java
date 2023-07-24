package ec.edu.espe.arquitectura.banquitowsgestionadmin.controller.dto;

import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.Country;
import ec.edu.espe.arquitectura.banquitowsgestionadmin.model.GeoLocation;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class HolidayRS {
    private String uniqueId;
    private Date holidayDate;
    private GeoLocation location;
    private Country country;
    private String name;
    private String type;
    private String state;
}
