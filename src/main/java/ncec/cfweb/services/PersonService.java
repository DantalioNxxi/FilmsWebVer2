package ncec.cfweb.services;

import java.util.List;
import ncec.cfweb.Gender;
import ncec.cfweb.Person;

/**
 *
 * @author DantalioNxxi
 */
public interface PersonService {
    
    Person addPersonWithMovies(Person person, List<Long> movieIds);
    
    Person addMoviesToPerson(Person person, List<Long> movieIds);
    
    Person addPerson(Person person);
    
    List<Person> getByFirstAndLastName(String firstname, String lastname);
    
    Person getById(Long id);
    
    Person editPerson(String oldfirstname, String oldlastname, String firstname, String lastname, int age, Gender gender);
    
    Person editPerson(Long id, String firstname, String lastname, int age, Gender gender);
    
    List<Person> getAll();
    
    void deleteByFirstAndLastName(String firstname, String lastname);
    
    void deleteById(Long id);
    
    
}
