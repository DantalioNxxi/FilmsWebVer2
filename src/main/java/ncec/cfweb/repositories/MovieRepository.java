package ncec.cfweb.repositories;

import ncec.cfweb.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
//@Repository
public interface MovieRepository extends MongoRepository<Movie, UUID> {
    
    List<Movie> findByTitle(String title);

    //@Modifying
    //@Transactional
    void deleteByTitle(String title);

    List<Movie> findByIdAndPersonsContains(UUID movieId, UUID personId);

    @Override
    //@Modifying
    //@Transactional
    void deleteById(UUID id);

    @Override
    List<Movie> findAll();

    @Override
    List<Movie> findAllById(Iterable<UUID> ids);
}
