package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "holidays")
public class Holiday {
    @Id
    private String id;
    private Date holidayDate;
    private GeoLocation location;
    private Country country;
    private String name;
    private String type;
    private String state;
    private Integer version;
}
