package ncec.cfweb.services;

import java.util.List;
import ncec.cfweb.Genre;

/**
 *
 * @author DantalioNxxi
 */
public interface GenreService {
    
    List<Genre> findByName(String name);
    
    Genre findById(Integer id);

    List<Genre> findAll();
    
}
