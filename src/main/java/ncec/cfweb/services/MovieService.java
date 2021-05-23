package ncec.cfweb.services;

import ncec.cfweb.entity.Movie;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */

public interface MovieService {
    
    Movie addMovie(String title, int duration, String description);//Date date, 
    
    Movie addMovie(Movie movie);
    
    Movie addMovieWithActorsAndGenres(Movie movie, List<UUID> actors, List<UUID> genreIds);
    
    void delete(String name);
    
    void deleteById(UUID id);
    
    List<Movie> getByName(String name);

    Set<Movie> findByIdAndPersonsContains(UUID movieId, UUID personId);
    
    Movie getById(UUID id);
    
    List<Movie> getAll();

    List<Movie> getByIds(Collection<UUID> movieIds);
    
    void exportMovies(List<UUID> movieIds, OutputStream out) throws IOException;
    
    List<Movie> importMovie(String movieName);
    
    void savingMovies(List<Movie> movies);
    
}
