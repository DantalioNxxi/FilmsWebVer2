
package ncec.cfweb.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.*;

/**
 *
 * @author DantalioNxxi
 */
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
@Document(collection = "movie")
@Getter
@Setter
public class Movie {
    
    @Id
    private UUID id;

    @XmlElement
    private String title;

    @DateTimeFormat (pattern="yyyy-MM-dd")
    //@Temporal(TemporalType.DATE)
    private Date dateCreation;

    private int duration;

    @XmlElement
    //@Column(length = 2500)
    private String description;
    
    //@ElementCollection
    //@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, targetEntity = Genre.class)
    private Set<Genre> genres;
    
    //@ElementCollection
//    //@ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY) //Movie - is owner
    //@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY) //Movie - is owner
    private Set<UUID> persons;
    
    //@ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Person director;
    
    //@Transient
    @XmlElement
    private String fullname;//tm
    
    //@Transient
    @XmlElement
    private String fulldate;//tm
    
    public Movie() {
        persons = new HashSet<>();
        genres = new HashSet<>();
    }
    
    public Movie(String title, Date date, int duration, String description) {
        this.title = title;
        this.dateCreation = date;
        this.duration = duration;
        persons = new HashSet<>();
        genres = new HashSet<>();
    }
    
    public Movie(String title, int duration, String description) {
        this.title = title;
        this.duration = duration;
        persons = new HashSet<>();
        genres = new HashSet<>();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.title);
        hash = 97 * hash + this.duration;
        hash = 97 * hash + Objects.hashCode(this.description);
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
        final Movie other = (Movie) obj;
        if (this.duration != other.duration) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.dateCreation, other.dateCreation)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Movie(" + title + ")";
    }
}