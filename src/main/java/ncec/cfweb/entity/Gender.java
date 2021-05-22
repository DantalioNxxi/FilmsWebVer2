package ncec.cfweb.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
@Document(collection = "gender")
@Data
@ToString
public class Gender {

    @Id
    private UUID id;

    @Column(unique = true)
    private String name;//must be is unique
}
