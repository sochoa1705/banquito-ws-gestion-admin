package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
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
