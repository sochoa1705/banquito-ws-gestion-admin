package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bankEntity")
@CompoundIndex(name = "idx_entidades_entityId_interCode", def = "{'id':1, 'internationalCode':1 }", unique = true)
public class BankEntity {

    @Id
    private String id;
    private String name;
    private String internationalCode;
    private List<Branch> branches;


  }
