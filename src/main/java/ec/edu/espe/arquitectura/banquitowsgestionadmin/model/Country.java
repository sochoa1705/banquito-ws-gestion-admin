package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "countries")
public class Country {
    @Id
    private String id;
    private String code;
    private String name;
    private String phoneCode;
    private String status;
    private List<GeoStructure> geoStructure;
}
