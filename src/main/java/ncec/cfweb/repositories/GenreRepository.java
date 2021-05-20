package ncec.cfweb.repositories;

import ncec.cfweb.entity.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author DantalioNxxi
 */
@Repository
public interface GenreRepository extends CrudRepository<Genre, Integer>{
    
    List<Genre> findByName(String name);

    @Override
    Optional<Genre> findById(Integer id);

    @Override
    List<Genre> findAll();
}
