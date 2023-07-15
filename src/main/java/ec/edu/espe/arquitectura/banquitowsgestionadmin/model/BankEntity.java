package ec.edu.espe.arquitectura.banquitowsgestionadmin.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor

@Document(collection = "entidadesBancarias")
@CompoundIndex(name = "idx_entidades_entityId_interCode", def = "{'id':1, 'internationalCode':1 }", unique = true)
public class BankEntity {

    @Id
    private String id;
    private String name;
    private String internationalCode;
    private List<Branch> branches;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((internationalCode == null) ? 0 : internationalCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BankEntity other = (BankEntity) obj;
        if (internationalCode == null) {
            if (other.internationalCode != null)
                return false;
        } else if (!internationalCode.equals(other.internationalCode))
            return false;
        return true;
    }

}
