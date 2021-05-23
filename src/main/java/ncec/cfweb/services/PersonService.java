package ncec.cfweb.services;

import ncec.cfweb.entity.Person;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
public interface PersonService {
    
    Person addPersonWithMovies(Person person, List<UUID> movieIds);
    
    Person addMoviesToPerson(Person person, List<UUID> movieIds);
    
    Person addPerson(Person person);
    
    List<Person> getByFirstAndLastName(String firstname, String lastname);
    
    Person getById(UUID id);
    
    Person editPerson(String oldfirstname, String oldlastname, String firstname, String lastname, int age, UUID genderId);
    
    Person editPerson(UUID id, String firstname, String lastname, int age, UUID genderId);
    
    List<Person> getAll();
    
    void deleteByFirstAndLastName(String firstname, String lastname);
    
    void deleteById(UUID id);
    
    
}
