package ncec.cfweb.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ncec.cfweb.entity.Movie;
import ncec.cfweb.services.GenreService;
import ncec.cfweb.services.MovieService;
import ncec.cfweb.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author DantalioNxxi
 */
@Controller
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    MovieService movieService;
    
    @Autowired
    GenreService genreService;
    
    @Autowired
    PersonService personService;
    
    private static final Logger LOG = LoggerFactory.getLogger(MovieController.class);
    
    //===========Movie Info=====================

    @GetMapping(value = "/movie-info/{movieId}")
    ModelAndView movieInfo(Model model, @PathVariable(value = "movieId") Long movieId){
        ModelAndView mv = new ModelAndView("movie/movie-info");
        Movie movie = movieService.getById(movieId);
        mv.addObject("movie", movie);
        LOG.info("Movie Info: ");
        LOG.info("Is movie has persons??: "+movie.getPersons().toString());
        mv.addObject("persons", movie.getPersons());
        mv.addObject("genres", movie.getGenres());
        return mv;
    }
    
    @PostMapping(value = "/movie-info{movieId}")
    ModelAndView afterEditInfoMovie(@PathVariable(value = "movieId") Long movieId,
            @RequestParam(value="movieName") String movieName){
        //check by parse for movieName
        Movie movie = movieService.getById(movieId);        //where is the checking must being?
        return new ModelAndView("movie/movie-info", "movie", movie);
    }
    
    //===========All Movies=====================
    
    @GetMapping(value = "/all-movies")
    public ModelAndView allMovies() {//Model model
        
        LOG.info("In GetMapping All-Movies: ");
        ModelAndView mv = new ModelAndView("movie/all-movies");
        mv.addObject("movies", movieService.getAll());
 
        return mv;
    }
    
    @PostMapping(value = "/all-movies")
    String allMoviesSearch(Model model,
            @RequestParam(value="movieName") String title,
//            @RequestParam(value="date") Date date, // special form for to fit a date????
            @RequestParam(value="duration") Integer duration,
            @RequestParam(value="description") String description){
        //check by parse for movieName, date, duration and description
        
        if (!movieService.getByName(title).isEmpty()){
            return "redirect: /movie/create-movie-miss-page?message=Фильм с таким именем существует!";
        }
        
        movieService.addMovie(title, duration, description); //date, 
        
        return "redirect:/movie/all-movies";
    }
    
    //===========Search Movies=====================
    
    @PostMapping(value = "/search-movie-page")
    String searchMovie(Model model, @RequestParam String movieName, RedirectAttributes redirectAttributes){
        LOG.info("Searching movie with name "+ movieName+ "...");
        redirectAttributes.addAttribute("movieName", movieName);
        
        List<Movie> movies = movieService.getAll();
        if (!movies.isEmpty()){
            for(Movie m : movies){
                if(m.getTitle().contains(movieName)){
                    LOG.info("Such film with name "+ movieName+ " was founded");
                    
                    return "redirect:/movie/search-movie-result-page";
                }
            }
        }
        
        model.addAttribute("movieName", movieName);
        return "movie/search-movie-miss-page";
    }

    @GetMapping(value = "/search-movie-result-page")
    ModelAndView searchMoviePage(@RequestParam (value = "movieName") String movieName){
        ModelAndView mv = new ModelAndView("movie/search-movie-result-page");
        List<Movie> movies = movieService.getAll();
        List<Movie> foundmovies = new ArrayList<>();
        for(Movie m : movies){
            if(m.getTitle().contains(movieName)){
                foundmovies.add(m);
            }
        }
        mv.addObject("movies", foundmovies);
        return mv;
    }

    @GetMapping(value = "/search-movie-miss-page{movieName}")
    String searchMovieMissPage(Model model, @PathVariable(value = "movieName") String movieName){
        model.addAttribute("movieName", movieName);
        return "movie/search-movie-miss-page";
    }
    
    //===========Create Movie=====================
    
    @GetMapping(value = "/create-movie-page")
    public ModelAndView showCreateMoviePage() {
        ModelAndView mv = new ModelAndView("movie/create-movie-page");
        mv.addObject("genres", genreService.findAll());
        mv.addObject("persons", personService.getAll());
        return mv;
    }

    @PostMapping(value = "/create-movie-page")
    String createMovie(@RequestParam(value="title") String title,
            @RequestParam(value="date") @DateTimeFormat(pattern="yyyy-MM-dd") Date date, // special form for to fit a date????
            @RequestParam(value="duration") Integer duration,
            @RequestParam(value="description") String description,
            @RequestParam(value="directorId", required = false) Long directorId,
            @RequestParam(value="genreIds", required = false) List<Integer> genreIds,
            @RequestParam(value="actorIds", required = false) List<Long> actorIds,
            Model model){
        
        LOG.info("In create movie page: ");
        ModelAndView mv;
        
        Movie newm = new Movie(title, duration, description);
        
        newm.setDateCreation(date);
        LOG.info("Date was setted!!!!!: "+date.toString());
        
        if (!movieService.getByName(title).isEmpty()){
            for(Movie m : movieService.getByName(title)){
                if (m.equals(newm)){
                    LOG.info("Such movie already exists: ");
                    return "redirect:/movie/create-movie-miss-page?message=Such movie already exists!";
                }
            }
        }
        
        if (directorId != null) newm.setDirector(personService.getById(directorId));
        if (actorIds == null) actorIds = new ArrayList<>();
        if (genreIds == null) genreIds = new ArrayList<>();
        
        movieService.addMovieWithActorsAndGenres(newm, actorIds, genreIds);//date,
        LOG.info("Return all-movies: ");
        mv = new ModelAndView("movie/all-movies");
        return "redirect:/movie/all-movies";
    }
    
    @GetMapping(value = "/create-movie-miss-page")
    public ModelAndView showCreateMovieMissPage(@RequestParam(value = "message") String message) {
        ModelAndView mv = new ModelAndView("movie/create-movie-miss-page");
        mv.addObject("message", message);
        return mv;
    }

    //===========Edit Movie=====================

    @GetMapping(value = "/edit-movie-page")
    ModelAndView getEditMovie(@RequestParam Long id) {
        ModelAndView mv = new ModelAndView("movie/edit-movie-page");
        mv.addObject("movie", movieService.getById(id));
        mv.addObject("genres", genreService.findAll());
        mv.addObject("persons", personService.getAll());
        return mv;
    }

    @PostMapping(value = "/edit-movie-page")
    ModelAndView postEditMovie(
            @RequestParam(value="id") Long id,
            @RequestParam(value="title") String title,
            @RequestParam(value="dateCreation") @DateTimeFormat(pattern="yyyy-MM-dd") Date dateCreation,
            @RequestParam(value="duration") Integer duration,
            @RequestParam(value="description") String description,
            @RequestParam(value="directorId", required = false) Long directorId,
            @RequestParam(value="genreIds", required = false) List<Integer> genreIds,
            @RequestParam(value="actorIds", required = false) List<Long> actorIds)
    {
        
        Movie newm = movieService.getById(id);
        newm.setTitle(title);
        newm.setDuration(duration);
        newm.setDescription(description);
        newm.setDateCreation(dateCreation);
        
        if (directorId != null) newm.setDirector(personService.getById(directorId));
        if (actorIds == null) actorIds = new ArrayList<>();
        if (genreIds == null) genreIds = new ArrayList<>();
        
        movieService.addMovieWithActorsAndGenres(newm, actorIds, genreIds);//date,
        
        ModelAndView mv = new ModelAndView("movie/movie-info");
        Movie editm = movieService.getById(id);
        mv.addObject("movie", editm);
        mv.addObject("persons", editm.getPersons());
        mv.addObject("genres", editm.getGenres());
        return mv;
    }
    
    @PostMapping(value = "/delete-movie")
    String deletePerson(@RequestParam(value="movieId") Long movieId)
    {
        LOG.info("Delete movie post controller: ");
        movieService.deleteById(movieId);
        return "redirect:/movie/all-movies";
    }
    
    //============Export============= 

    @PostMapping("/export-movies")
    public ResponseEntity<StreamingResponseBody> exportMovies(@RequestParam List<Long> movieIds){
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"movies.csv\"")
                .body(out -> movieService.exportMovies(movieIds, out));
    }
    
    //============Import=============
    
    @PostMapping("/import-movie")
    RedirectView importPage(@RequestParam String movieName, RedirectAttributes redirectAttributes){
        LOG.info("In import-movie controller:");
        ArrayList<Movie> movies = (ArrayList<Movie>)movieService.importMovie(movieName);//ver2
        redirectAttributes.addFlashAttribute("movies", movies);
        RedirectView redirectView = new RedirectView("/movie/import-movie-result");
        redirectView.getAttributesMap().put("movies", movies);
        LOG.info("Try save movies in controller:");
        movieService.savingMovies(movies);
        return redirectView;
    }
    
    @GetMapping(value = "/import-movie-result")
    ModelAndView importMovieResult(RedirectAttributes redirectAttributes){
        LOG.info("Saving importe-movie-result-controller!");
        return new ModelAndView("import/import-movie-page");//.addObject("movies", movies)
    }
    
}
