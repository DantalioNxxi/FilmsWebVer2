package ncec.cfweb.services.impl;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import ncec.cfweb.entity.Genre;
import ncec.cfweb.entity.Movie;
import ncec.cfweb.entity.Movies;
import ncec.cfweb.entity.Person;
import ncec.cfweb.repositories.MovieRepository;
import ncec.cfweb.services.MovieService;
import ncec.cfweb.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.tidy.Tidy;

import javax.xml.bind.JAXB;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DantalioNxxi
 */
@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final PersonService personService;
    private final GenreServiceImpl genreService;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, PersonService personService,
                            GenreServiceImpl genreService) {
        this.movieRepository = movieRepository;
        this.personService = personService;
        this.genreService = genreService;
    }

    @Override
    public void deleteById(UUID id) {
        movieRepository.deleteById(id);
        log.info("Movie has deleted!");
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
    public Movie addMovieWithActorsAndGenres(Movie movie, List<UUID> actors, List<UUID> genreIds) {
        log.info("In addMovieWithActors: ");

//        Set<Person> actorset = new HashSet<>();
//        if (!actors.isEmpty())
//        {
//            for (UUID idc : actors){
//                actorset.add(personService.getById(idc));
//            }
//        }
        movie.setPersons(new HashSet<>(actors));
        log.info("Actor's Set was added: ");

        Set<Genre> genreset = new HashSet<>();
        if (!genreIds.isEmpty()) {
            for (UUID idc : genreIds) {
                genreset.add(genreService.findById(idc));
            }
        }
        movie.setGenres(genreset);
        log.info("Genre's Set was added: ");

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
    public Movie getById(UUID id) {
        return movieRepository.findById(id).get();
    }

    @Override
    public Set<Movie> findByIdAndPersonsContains(UUID movieId, UUID personId) {
//        Person person = personService.getByFirstAndLastName(firstname, lastname).get(0);
////        log.info("Try to cast getMovies: ");
////        HashSet<Movie> hsm = (HashSet<Movie>)pp.getMovies();
//        log.info("The films was added: "+person.getMovies().toString());
//        if (person.getMovies().isEmpty()){System.out.println("IS EMPTY!");}
//        for(Movie movie : person.getMovies()){
//            log.info("Name of film: "+movie.getTitle());
//        }
        List<Movie> movies = movieRepository.findByIdAndPersonsContains(movieId, personId);
        return new HashSet<>(movies);
    }

    @Override
    public List<Movie> getAll() {
        return movieRepository.findAll();//is permit?
    }

    @Override
    public List<Movie> getByIds(Collection<UUID> movieIds) {
        return movieRepository.findAllById(movieIds);
    }

    @Override
    public void exportMovies(List<UUID> movieIds, OutputStream out) throws IOException {
        List<Movie> movies = getByIds(movieIds);
        try (CSVWriter csv = new CSVWriter(new OutputStreamWriter(out))) {

            String[] line = new String[7];
            for (Movie movie : movies) {
                line[0] = movie.getTitle();
                line[1] = movie.getDateCreation() == null ? "" : movie.getDateCreation().toString();
//                line[1] =  movie.getDateCreation()==null ? "" : movie.getDateCreation().toString();
                line[2] = Integer.toString(movie.getDuration());
                line[3] = movie.getDescription() == null ? "" : movie.getDescription();
                if (movie.getGenres() == null || movie.getGenres().isEmpty()) {
                    line[4] = "";
                } else {
                    line[4] = movie.getGenres().toString();
                }
                line[5] = movie.getDirector() == null ? "" : movie.getDirector().getFirstname();
                line[6] = movie.getDirector() == null ? "" : movie.getDirector().getLastname();
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

        log.warn("xml = {}", xml.toString());

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


        try (FileWriter fw = new FileWriter("afterXsltForShow.txt", false)) {//log after xsl-handle:
            fw.write(afterXslt.toString());
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

        log.warn("afterXslt = {}", afterXslt.toString());

        Movies movies = JAXB.unmarshal(new StringReader(afterXslt.toString()), Movies.class);// Movies

        // First version: save Repository
        // and then return

        log.info("Change dates!");
        List<Movie> retMovies = movies.getMovies();//ver2
//        ArrayList<Movie> retMovies = (ArrayList<Movie>)movies.getMovies();//ver2

        Pattern yearPattern = Pattern.compile("([0-9]{4})");//check dates of movies
        for (Movie movie : retMovies) {
            Matcher m = yearPattern.matcher(movie.getFulldate());
            if (m.find()) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.valueOf(m.group()), 0, 1);
                movie.setDateCreation(calendar.getTime());
                if (log.isDebugEnabled()) {
                    log.debug("Update movie DateCreation to ({})", movie.getDateCreation());
                }
            }
        }

        log.warn("movies = {retMovies.size}", retMovies.size());


        return retMovies;
    }

    @Override
    public void savingMovies(List<Movie> movies) {
        //save movies:
        log.info("Saving imported movies!", movies.size());
//        Map<Long, Movie> movieMap = retMovies.stream()
//                .collect(HashMap::new, (hashMap, movie) -> hashMap.put(movie.getId(), movie), HashMap::putAll);

        for (Movie movie : movies) {
            if (movieRepository.findByTitle(movie.getTitle()).isEmpty()) {

//                parse director
                log.info("Try parse director!");
                String[] str;
                if (movie.getFullname().contains(" ")) {
                    log.info("Contains _!");
                    str = movie.getFullname().split(" ");
                    if (personService.getByFirstAndLastName(str[0], str[1]).isEmpty()) {
                        Person person = new Person(str[0], str[1]);
                        movie.setDirector(personService.addPerson(person));
                        person.addMovie(movie);//tm
                    } else {
                        movie.setDirector(personService.getByFirstAndLastName(str[0], str[1]).get(0));
                        personService.getByFirstAndLastName(str[0], str[1]).get(0).addMovie(movie);//tm
                    }
                } else {
                    log.info("Not contains _!");
                    str = new String[1];
                    str[0] = movie.getFullname();
                    if (personService.getByFirstAndLastName(str[0], "defaultLastname").isEmpty()) {
                        Person person = new Person(str[0], "defaultLastname");
                        movie.setDirector(personService.addPerson(person));
                        person.addMovie(movie);//tm
                    } else {
                        movie.setDirector(personService.getByFirstAndLastName(str[0], "defaultLastname").get(0));//tm
                        personService.getByFirstAndLastName(str[0], "defaultLastname").get(0).addMovie(movie);//tm
                    }
                }
                log.info("Directors successfully were saved!");


                //parse date
                //                Integer date = Integer.parseInt(parseDateCreation(movie.getFulldate()));
                //                movie.setDateCreation(date);//or made to Integer?
                movieRepository.save(movie);
            }
        }
        log.info("Movies was saved!", movies.size());
    }
    //END of class
}
