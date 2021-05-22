package ncec.cfweb.repositories;

import ncec.cfweb.entity.Movie;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
@Repository
public interface MovieRepository extends MongoRepository<Movie, UUID>,
        CrudRepository<Movie, UUID> {
    
    List<Movie> findByTitle(String name);

    @Override
    Optional<Movie> findById(UUID id);

    @Modifying
    @Transactional
    void deleteByTitle(String title);

    @Override
    @Modifying
    @Transactional
    void deleteById(UUID id);

    @Override
    List<Movie> findAll();

    @Override
    List<Movie> findAllById(Iterable<UUID> var1);
}
