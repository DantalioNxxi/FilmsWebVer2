package ncec.cfweb.services;

import ncec.cfweb.entity.Genre;

import java.util.List;

/**
 *
 * @author DantalioNxxi
 */
public interface GenreService {
    
    List<Genre> findByName(String name);
    
    Genre findById(Integer id);

    List<Genre> findAll();
    
}
