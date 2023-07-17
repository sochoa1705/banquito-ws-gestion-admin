package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "geoLocation")
public class GeoLocation {
    @Id
    private String id;
    private String name;
    private String areaPhoneCode;
    private String zipCode;
    private String locationParent;
}
