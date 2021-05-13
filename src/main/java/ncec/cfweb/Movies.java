package ncec.cfweb;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VYZH
 * @since 06.04.2018
 */
@XmlRootElement
public class Movies {

    List<Movie> movies = new ArrayList<>();
    
    List<String> directors = new ArrayList<>();

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> director) {
        this.directors = director;
    }
    
    
}
