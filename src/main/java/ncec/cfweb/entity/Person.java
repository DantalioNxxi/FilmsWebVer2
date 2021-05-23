
package ncec.cfweb.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
////@Entity
@ToString
@Getter
@Setter
@Document(collection = "person")
public class Person {

    @Id
    //@Column
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    //@Column(name = "firstname")
    private String firstname;

    //@Column(name = "lastname")
    private String lastname;

    //@Column(name = "age")
    private int age;

    private UUID genderId;

    //@ElementCollection
    //@ManyToMany(mappedBy = "persons", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)//tm
    private Set<UUID> movies;

    public Person() {
        movies = new HashSet<>();
    }

    public Person(String firstname, String lastname, int age, UUID genderId) {
        this.genderId = genderId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        movies = new HashSet<>();
    }

    public Person(int age, String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        movies = new HashSet<>();
    }

    public Person(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
        movies = new HashSet<>();
    }

    public void addMovie(Movie m){
        movies.add(m.getId());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.firstname);
        hash = 67 * hash + Objects.hashCode(this.lastname);
        hash = 67 * hash + this.age;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (this.age != other.age) {
            return false;
        }
        if (!Objects.equals(this.firstname, other.firstname)) {
            return false;
        }
        if (!Objects.equals(this.lastname, other.lastname)) {
            return false;
        }
        if (this.genderId != other.genderId) {
            return false;
        }
        return true;
    }

}
