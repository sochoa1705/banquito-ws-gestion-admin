package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Builder
@Document(collection = "geoStructure")
public class GeoStructure {
    @Id
    private String id;
    private Integer levelCode;
    private String name;
    private String state;
    private Country country;
    private List<GeoLocation> locations;

}
