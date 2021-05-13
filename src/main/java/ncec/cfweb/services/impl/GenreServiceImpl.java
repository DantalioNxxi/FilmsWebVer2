package ncec.cfweb.services.impl;

import java.util.List;
import ncec.cfweb.Genre;
import ncec.cfweb.repositories.GenreRepository;
import ncec.cfweb.services.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author DantalioNxxi
 */
@Service
public class GenreServiceImpl implements GenreService{
    
    private static final Logger LOG = LoggerFactory.getLogger(GenreServiceImpl.class);
    
    @Autowired
    GenreRepository genreRepository;

    @Override
    public List<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public Genre findById(Integer id) {
        return genreRepository.findById(id);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }
    
}
