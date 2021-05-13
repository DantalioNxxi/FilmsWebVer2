package ncec.cfweb.services.impl;

import ncec.cfweb.Movie;
import ncec.cfweb.Movies;
import ncec.cfweb.Person;
import ncec.cfweb.repositories.MovieRepository;
import ncec.cfweb.services.MovieService;
import ncec.cfweb.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.opencsv.CSVWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.w3c.tidy.Tidy;

import javax.xml.bind.JAXB;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ncec.cfweb.Genre;

/**
 *
 * @author DantalioNxxi
 */
@Service
public class MovieServiceImpl implements MovieService{

    private static final Logger LOG = LoggerFactory.getLogger(MovieServiceImpl.class);
    
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    PersonService personService;
    
    @Autowired
    GenreServiceImpl genreService;

    @Override
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
        LOG.info("Movie has deleted!");
    }
    
    @Override
    public Movie addMovie(String title, int duration, String description) {
        //cheking for having such film in db
        Movie movie = new Movie();
        movie.setTitle(title);
//        movie.setDateCreation(date);
        movie.setDuration(duration);
        movie.setDescription(description);
        
//        get sets and : if set.not null then add this persons
        
        return movieRepository.save(movie);
    }

    @Override
    public Movie addMovieWithActorsAndGenres(Movie movie, List<Long> actors, List<Integer> genreIds) {
        LOG.info("In addMovieWithActors: ");
        
        Set<Person> actorset = new HashSet<>();
        if (!actors.isEmpty())
        {
            for (Long idc : actors){
                actorset.add(personService.getById(idc));
            }
        }
        movie.setPersons(actorset);
        LOG.info("Actor's Set was added: ");
        
        Set<Genre> genreset = new HashSet<>();
        if(!genreIds.isEmpty()){
            for (Integer idc : genreIds){
                genreset.add(genreService.findById(idc));
            }
        }
        movie.setGenres(genreset);
        LOG.info("Genre's Set was added: ");
        
        return movieRepository.save(movie);
    }
    
    @Override
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }
    
    @Override
    public void delete(String name) {//still without checking!
//        movieRepository.delete(movieRepository.findByName(name));
        movieRepository.deleteByTitle(name);
    }

    @Override
    public List<Movie> getByName(String name) {
        return movieRepository.findByTitle(name);
    }

    @Override
    public Movie getById(Long id) {
        return movieRepository.findById(id);
    }
    

    @Override
    public List<Movie> getAll() {
        return movieRepository.findAll();//is permit?
    }
    
    @Override
    public List<Movie> getByIds(Collection<Long> movieIds) {
        return (List<Movie>) movieRepository.findAll(movieIds);
    }
    
    @Override
    public void exportMovies(List<Long> movieIds, OutputStream out) throws IOException {
        List<Movie> movies = getByIds(movieIds);
        try (CSVWriter csv = new CSVWriter(new OutputStreamWriter(out))) {
            
            String[] line = new String[7];
            for (Movie movie : movies) {
                line[0] =  movie.getTitle();
                line[1] = movie.getDateCreation()==null?"":movie.getDateCreation().toString();
//                line[1] =  movie.getDateCreation()==null ? "" : movie.getDateCreation().toString();
                line[2] =  Integer.toString(movie.getDuration());
                line[3] =  movie.getDescription()==null?"":movie.getDescription();
                if (movie.getGenres()==null||movie.getGenres().isEmpty()){
                    line[4] = "";
                } else {
                    line[4] =  movie.getGenres().toString();
                }
                line[5] =  movie.getDirector()==null ? "" : movie.getDirector().getFirstname();
                line[6] =  movie.getDirector()==null ? "" : movie.getDirector().getLastname();
                csv.writeNext(line, true);
            }
        }
    }

    @Override
    public List<Movie> importMovie(String movieName) {
        
        RestTemplate restTemplate = new RestTemplate();// http request
        String html = restTemplate
                .getForObject("http://www.imdb.com/search/title?title={0}&title_type=feature,tv_movie,tv_series",
                        String.class, movieName);

        
        Tidy tidy = new Tidy();// sanitize JTidy
        tidy.setXmlOut(true);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        tidy.setNumEntities(true);
        tidy.setForceOutput(true);

        StringWriter xml = new StringWriter();
        tidy.parse(new StringReader(html), xml);

        LOG.warn("xml = {}", xml.toString());
        
        StringWriter afterXslt = new StringWriter();// XSLT
        try {
            InputStream xsl = getClass().getResourceAsStream("/import.xsl");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(new StreamSource(xsl));
            transformer.transform(new StreamSource(new StringReader(xml.toString())), new StreamResult(afterXslt));
        } catch (TransformerConfigurationException e) {
            throw new IllegalStateException(e);
        } catch (TransformerException e) {
            throw new IllegalStateException(e);
        }
        
        
        try(FileWriter fw = new FileWriter("afterXsltForShow.txt", false)){//log after xsl-handle:
              fw.write(afterXslt.toString());
        } catch (IOException ex){
            throw new IllegalStateException(ex);
        }

        LOG.warn("afterXslt = {}", afterXslt.toString());

        Movies movies = JAXB.unmarshal(new StringReader(afterXslt.toString()), Movies.class);// Movies

        // First version: save Repository
        // and then return

        LOG.info("Change dates!");
        List<Movie> retMovies = movies.getMovies();//ver2
//        ArrayList<Movie> retMovies = (ArrayList<Movie>)movies.getMovies();//ver2

        Pattern yearPattern = Pattern.compile("([0-9]{4})");//check dates of movies
        for (Movie movie : retMovies) {
            Matcher m = yearPattern.matcher(movie.getFulldate());
            if (m.find()) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.valueOf(m.group()), 0, 1);
                movie.setDateCreation(calendar.getTime());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Update movie DateCreation to ({})", movie.getDateCreation());
                }
            }
        }

        LOG.warn("movies = {retMovies.size}", retMovies.size());
        
        
        return retMovies;
    }
    
    @Override
    public void savingMovies(List<Movie> movies){
        //save movies:
        LOG.info("Saving imported movies!", movies.size());
//        Map<Long, Movie> movieMap = retMovies.stream()
//                .collect(HashMap::new, (hashMap, movie) -> hashMap.put(movie.getId(), movie), HashMap::putAll);

        for (Movie movie : movies){
            if (movieRepository.findByTitle(movie.getTitle()).isEmpty()){

//                parse director
                LOG.info("Try parse director!");
                String[] str;
                if (movie.getFullname().contains(" ")){
                    LOG.info("Contains _!");
                     str = movie.getFullname().split(" ");
                        if (personService.getByFirstAndLastName(str[0], str[1]).isEmpty()){
                           Person person = new Person(str[0], str[1]);
                           movie.setDirector(personService.addPerson(person));
                           person.addMovie(movie);//tm
                       } else {
                           movie.setDirector(personService.getByFirstAndLastName(str[0], str[1]).get(0));
                           personService.getByFirstAndLastName(str[0], str[1]).get(0).addMovie(movie);//tm
                       }
                } else {
                    LOG.info("Not contains _!");
                    str = new String[1];
                    str[0] = movie.getFullname();
                    if (personService.getByFirstAndLastName(str[0], "defaultLastname").isEmpty()){
                           Person person = new Person(str[0], "defaultLastname");
                           movie.setDirector(personService.addPerson(person));
                           person.addMovie(movie);//tm
                       } else {
                           movie.setDirector(personService.getByFirstAndLastName(str[0], "defaultLastname").get(0));//tm
                           personService.getByFirstAndLastName(str[0], "defaultLastname").get(0).addMovie(movie);//tm
                    }
                }
                LOG.info("Directors successfully were saved!");
                

                //parse date
    //                Integer date = Integer.parseInt(parseDateCreation(movie.getFulldate()));
    //                movie.setDateCreation(date);//or made to Integer?
                movieRepository.save(movie);
            }
        }
        LOG.info("Movies was saved!", movies.size());
    }
    //END of class
}
