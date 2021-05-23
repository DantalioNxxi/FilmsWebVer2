package ncec.cfweb.repositories;

import ncec.cfweb.entity.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
//@Repository
public interface GenreRepository extends MongoRepository<Genre, UUID> {
    
    List<Genre> findByName(String name);

    //@Override
    //Optional<Genre> findById(UUID id);

//    @Override
//    List<Genre> findAll();
}
