package ncec.cfweb.repositories;

import java.util.List;
import javax.transaction.Transactional;
import ncec.cfweb.Movie;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DantalioNxxi
 */
@Repository
public interface MovieRepository extends CrudRepository<Movie, Long>{
    
    List<Movie> findByTitle(String name);
    
    Movie findById(Long id);

    @Modifying
    @Transactional
    void deleteByTitle(String title);
    
    @Modifying
    @Transactional
    void deleteById(Long id);

    @Override
    List<Movie> findAll();
    
}
