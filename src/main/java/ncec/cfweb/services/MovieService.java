package ncec.cfweb.services;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import ncec.cfweb.Movie;
import ncec.cfweb.Movies;

/**
 *
 * @author DantalioNxxi
 */

public interface MovieService {
    
    Movie addMovie(String title, int duration, String description);//Date date, 
    
    Movie addMovie(Movie movie);
    
    Movie addMovieWithActorsAndGenres(Movie movie, List<Long> actors, List<Integer> genreIds);
    
    void delete(String name);
    
    void deleteById(Long id);
    
    List<Movie> getByName(String name);
    
    Movie getById(Long id);
    
    List<Movie> getAll();

    List<Movie> getByIds(Collection<Long> movieIds);
    
    void exportMovies(List<Long> movieIds, OutputStream out) throws IOException;
    
    List<Movie> importMovie(String movieName);
    
    void savingMovies(List<Movie> movies);
    
}
