package ncec.cfweb.services;

import ncec.cfweb.entity.Genre;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
public interface GenreService {
    
    List<Genre> findByName(String name);
    
    Genre findById(UUID id);

    List<Genre> findAll();
    
}
