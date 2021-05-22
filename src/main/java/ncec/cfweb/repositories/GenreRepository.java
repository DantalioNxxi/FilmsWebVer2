package ncec.cfweb.repositories;

import ncec.cfweb.entity.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
@Repository
public interface GenreRepository extends MongoRepository<Genre, UUID>,
        CrudRepository<Genre, UUID> {
    
    List<Genre> findByName(String name);

    @Override
    Optional<Genre> findById(UUID id);

    @Override
    List<Genre> findAll();
}
