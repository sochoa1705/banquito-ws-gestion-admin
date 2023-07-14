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
    @Field("area_phone_code")
    private String areaPhoneCode;
    @Field("zip_code")
    private String zipCode;
    private String locationParent;
}
