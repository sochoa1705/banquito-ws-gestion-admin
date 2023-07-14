package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Builder;
import lombok.Data;

@Data
@Document(collection="holidays")
@Builder
public class Holiday {

    @Id
    private String id;
    @Field("holiday_date")
    private Date holidayDate;
    @DBRef
    private GeoLocation location;
    @DBRef 
    private Country country;
    private String name;
    private String type;
    private String status;
    private Integer version;

}
