package ncec.cfweb.services.impl;

import ncec.cfweb.entity.Movie;
import ncec.cfweb.entity.Person;
import ncec.cfweb.entity.Gender;
import ncec.cfweb.repositories.MovieRepository;
import ncec.cfweb.repositories.PersonRepository;
import ncec.cfweb.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author DantalioNxxi
 */
@Service
public class PersonServiceImpl implements PersonService{
    
    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    PersonRepository personRepository;
    
    @Autowired
    MovieRepository movieRepository;

    @Override
    public void deleteByFirstAndLastName(String firstname, String lastname) {
        LOG.info("Delete person through ServiceImpl...");
        personRepository.deleteByFirstnameAndLastname(firstname, lastname);
    }

    @Override
    public Person getById(Long id) {
        return personRepository.findById(id).get();
    }

    @Override
    public Person editPerson(Long id, String firstname, String lastname, int age, Gender gender) {
        Person newp = personRepository.findById(id).get();
        newp.setFirstname(firstname);
        newp.setLastname(lastname);
        newp.setAge(age);
        newp.setGender(gender);
        return personRepository.save(newp);
    }

    @Override
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }
    
    
    //possible to add method for to save a person

    @Override
    public Person addPerson(Person person) {
        return personRepository.save(person);
    }
    
    
    @Override
    public Person addPersonWithMovies(Person person, List<Long> movieIds) {
        
        if (!movieIds.isEmpty()){
            
            LOG.info("Start to add films");
//            Set<Movie> sm = new HashSet<>();
            for (Long mid : movieIds){
                LOG.info("Add Film " + movieRepository.findById(mid).get().getTitle());
//                sm.add(movieRepository.findById(mid));
                    person.addMovie(movieRepository.findById(mid).get());
            }
//            person.setMovies(sm);
            LOG.info("Set new set of films");
        }
        return personRepository.save(person);// tm not return still wothout collections
    }

    @Override//here is THE ERROR!
    public Person addMoviesToPerson(Person person, List<Long> movieIds) {
        LOG.info("Start to add new film collection");
        Set<Movie> sm = new HashSet<>();
        if (!movieIds.isEmpty()){
            for (Long mid : movieIds){
                LOG.info("Add Film " + movieRepository.findById(mid).get().getTitle());
                sm.add(movieRepository.findById(mid).get());
            }
        }
        person.setMovies(sm);
        LOG.info("Set new set of films");
        return personRepository.save(person);// tm not return still wothout collections
    }
    
    @Override
    public List<Person> getByFirstAndLastName(String firstname, String lastname) {
        return personRepository.findByFirstnameAndLastname(firstname, lastname);
    }

    @Override
    public Person editPerson(String oldfirstname, String oldlastname,
            String firstname, String lastname, int age, Gender gender) {
        
        Person person = personRepository.findByFirstnameAndLastname(oldfirstname, oldlastname).get(0);
        person.setAge(age);
        person.setGender(gender); //and films...
        Person newp = personRepository.save(person);
        
        if (!oldfirstname.equals(firstname) || !oldlastname.equals(lastname)){
            
            //костыль:
            Set<Movie> oldmovies= new HashSet<>();
            LOG.info("Check old films!");
            if (!newp.getMovies().isEmpty()){
                LOG.info("Old films was safed!");
                oldmovies.addAll(newp.getMovies());
                newp.getMovies().clear();
                LOG.info("Films of entity was cleared!");
            }
            
            personRepository.updateByFirstnameAndLastname(firstname, lastname, oldfirstname, oldlastname);
            LOG.info("Old fname and lname was changed");
            
            newp.setMovies(oldmovies);//костыль
            LOG.info("Old films was added again!");
        }
        
        return personRepository.findByFirstnameAndLastname(firstname, lastname).get(0);
    }


    @Override
    public List<Person> getAll() {
        return (List<Person>)personRepository.findAll();
    }
}
