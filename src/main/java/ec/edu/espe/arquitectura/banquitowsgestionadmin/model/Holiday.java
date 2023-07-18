package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "holidays")
@CompoundIndex(name = "holiday_index", def = "{'holidayDate': 1, 'name': 1, 'type': 1, 'state': 1}", unique = true)
public class Holiday {
    @Id
    private String id;
    @Indexed(unique = true)
    private String uniqueId;
    private Date holidayDate;
    private GeoLocation location;
    private Country country;
    private String name;
    private String type;
    private String state;
}
