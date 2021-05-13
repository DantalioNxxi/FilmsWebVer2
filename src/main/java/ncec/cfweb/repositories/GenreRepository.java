package ncec.cfweb.repositories;

import java.util.List;
import ncec.cfweb.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DantalioNxxi
 */
@Repository
public interface GenreRepository extends CrudRepository<Genre, Integer>{
    
    List<Genre> findByName(String name);
    
    Genre findById(Integer id);

    @Override
    List<Genre> findAll();
}
