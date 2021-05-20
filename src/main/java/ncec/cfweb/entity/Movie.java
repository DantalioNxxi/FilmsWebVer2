
package ncec.cfweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author DantalioNxxi
 */
@Entity
@XmlType
@XmlAccessorType(XmlAccessType.NONE)
public class Movie {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "MOVIE_ID")
    private Long id;

    @XmlElement
    private String title;

    @DateTimeFormat (pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    private int duration;

    @XmlElement
    @Column(length = 2500)
    private String description;
    
    @ElementCollection
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, targetEntity = Genre.class)
    private Set<Genre> genres;
    
    @ElementCollection
//    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY) //Movie - is owner
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY) //Movie - is owner
    private Set<Person> persons;
    
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Person director;
    
    @Transient
    @XmlElement
    private String fullname;//tm
    
    @Transient
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getDirector() {
        return director;
    }

    public void setDirector(Person director) {
        this.director = director;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFulldate() {
        return fulldate;
    }

    public void setFulldate(String fulldate) {
        this.fulldate = fulldate;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
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