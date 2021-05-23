package ncec.cfweb.repositories;

import ncec.cfweb.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
//@Repository
public interface PersonRepository extends MongoRepository<Person, UUID> {
    
    List<Person> findByFirstnameAndLastname(String firstname, String lastname);

//    @Override
//    Optional<Person> findById(UUID id);

    //@Modifying(clearAutomatically = true)
    //@Transactional
    //@Query("update Person p set p.firstname = ?1, p.lastname = ?2 where p.firstname = ?3 and p.lastname=?4")
    //void updateByFirstnameAndLastname(String firstname, String lastname, String oldfirstname, String oldlastname);
    
    //@Modifying
    //@Transactional
//    @Override
//    void deleteById(UUID id);
    
    //@Modifying
    //@Transactional
    void deleteByFirstnameAndLastname(String firstname, String lastname);
    
    
}
