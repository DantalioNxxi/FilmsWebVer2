package ncec.cfweb.repositories;

import ncec.cfweb.entity.Movie;
import org.springframework.data.jpa.repository.Modifying;
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
public interface MovieRepository extends CrudRepository<Movie, Long>{
    
    List<Movie> findByTitle(String name);

    @Override
    Optional<Movie> findById(Long id);

    @Modifying
    @Transactional
    void deleteByTitle(String title);

    @Override
    @Modifying
    @Transactional
    void deleteById(Long id);

    @Override
    List<Movie> findAll();

    @Override
    List<Movie> findAllById(Iterable<Long> var1);
}
