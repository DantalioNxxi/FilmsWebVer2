package ncec.cfweb.services.impl;

import ncec.cfweb.entity.Genre;
import ncec.cfweb.repositories.GenreRepository;
import ncec.cfweb.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
@Service
public class GenreServiceImpl implements GenreService{
    
    @Autowired
    GenreRepository genreRepository;

    @Override
    public List<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public Genre findById(UUID id) {
        return genreRepository.findById(id).get();
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }
    
}
