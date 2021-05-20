package ncec.cfweb.repositories;

import ncec.cfweb.entity.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author DantalioNxxi
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, Long>{
    
    List<Person> findByFirstnameAndLastname(String firstname, String lastname);

    @Override
    Optional<Person> findById(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Person p set p.firstname = ?1, p.lastname = ?2 where p.firstname = ?3 and p.lastname=?4")
    void updateByFirstnameAndLastname(String firstname, String lastname, String oldfirstname, String oldlastname);
    
    @Modifying
    @Transactional
    void deleteById(Long id);
    
    @Modifying
    @Transactional
    void deleteByFirstnameAndLastname(String firstname, String lastname);
    
    
}
