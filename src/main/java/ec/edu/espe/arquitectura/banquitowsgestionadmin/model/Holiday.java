package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Builder;
import lombok.Data;

@Data
@Document(collection="holidays")
@Builder
public class Holiday {

    @Id
    private String id;
    private Date holidayDate;
    @DBRef
    private GeoLocation locationId;
    @DBRef 
    private Country countryId;
    private String name;
    private String type;
    private String status;
    private Integer version;

}
