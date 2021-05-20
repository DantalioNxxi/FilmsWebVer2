package ncec.cfweb.controllers;

import ncec.cfweb.entity.Gender;
import ncec.cfweb.entity.Movie;
import ncec.cfweb.entity.Person;
import ncec.cfweb.services.MovieService;
import ncec.cfweb.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DantalioNxxi
 */
@Controller
@RequestMapping("/person")
public class PersonController {
    
    @Autowired
    PersonService personService;
    
    @Autowired
    MovieService movieService;
    
    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);
    
    //===========All Persons=====================
    
    @GetMapping(value = "/all-persons")
    public ModelAndView allPersons(Model model){
        
        ModelAndView mv = new ModelAndView("person/all-persons");
        mv.addObject("persons", personService.getAll());
        
        return mv;
    }
    
    //===========Person Info=====================
    
    @GetMapping(value = "/person-info")
    ModelAndView personInfo(Model model, 
        @RequestParam(value = "lastname") String lastname,
        @RequestParam(value = "firstname") String firstname) {
        
        ModelAndView mv = new ModelAndView("person/person-info");
        mv.addObject("person", personService.getByFirstAndLastName(firstname, lastname).get(0));
        mv.addObject("movies", personService.getByFirstAndLastName(firstname, lastname).get(0).getMovies());
        
        Person pp = personService.getByFirstAndLastName(firstname, lastname).get(0);
//        LOG.info("Try to cast getMovies: ");
//        HashSet<Movie> hsm = (HashSet<Movie>)pp.getMovies();
        LOG.info("The films was added: "+pp.getMovies().toString());
        if (pp.getMovies().isEmpty()){System.out.println("IS EMPTY!");}
        for(Movie movie : pp.getMovies()){
            LOG.info("Name of film: "+movie.getTitle());
        }
        return mv;
    }
    
    //===========Search Persons=====================
    
    @RequestMapping(value = "/search-person-page", method = RequestMethod.POST)
    String searchPerson(Model model, @RequestParam String fullName, RedirectAttributes redirectAttributes){
        LOG.info("Searching movie with name "+ fullName+ "...");
        redirectAttributes.addAttribute("fullName", fullName);
        
        List<Person> persons = personService.getAll();
        if (!persons.isEmpty()){
            for(Person p : persons){
                if(p.getFirstname().concat(" ").concat(p.getLastname()).contains(fullName)){
                    LOG.info("Such person with name "+ fullName+ " was founded");
                    return "redirect:/person/search-person-result-page";
                }
            }
        }
        
        model.addAttribute("fullName", fullName);
        return "person/search-person-miss-page";
    }
    
    @GetMapping(value = "/search-person-result-page")
    ModelAndView searchPersonResultPage(@RequestParam (value = "fullName") String fullName){
        ModelAndView mv = new ModelAndView("person/search-person-result-page");
        List<Person> persons = personService.getAll();
        List<Person> foundpersons = new ArrayList<>();
        for(Person p : persons){
            if(p.getFirstname().concat(" ").concat(p.getLastname()).contains(fullName)){
                foundpersons.add(p);
            }
        }
        mv.addObject("persons", foundpersons);
        return mv;
    }
    
    @RequestMapping(value = "/search-person-miss-page", method = RequestMethod.GET)
    @ResponseBody
    String searchPersonMissPage(Model model, @PathVariable(value = "fullName") String fullName){
        model.addAttribute("fullName", fullName);
        return "person/search-person-miss-page";
    }
    
    //===========Edit Person=====================
    
    @GetMapping(value = "/edit-person-page")
    ModelAndView getEditPerson(
        @RequestParam(value = "lastname") String lastname,
        @RequestParam(value = "firstname") String firstname){
        ModelAndView mv = new ModelAndView("person/edit-person-page");
        mv.addObject("oldfirstname", firstname);
        mv.addObject("oldlastname", lastname);
        mv.addObject("person", personService.getByFirstAndLastName(firstname, lastname).get(0));
        mv.addObject("movies", movieService.getAll());
        return mv;
    }

    @PostMapping(value = "/edit-person-page")
    ModelAndView postEditPerson(
            @RequestParam(value="oldfirstname") String oldfirstname,
            @RequestParam(value="oldlastname") String oldlastname,
            @RequestParam(value="firstname") String firstname,
            @RequestParam(value="lastname") String lastname,
            @RequestParam(value="age") int age,
            @RequestParam(value="gender") Gender gender,
            @RequestParam(value="movieIds", required = false) List<Long> movieIds)
    {
        
        LOG.info("The oldfirstname: "+oldfirstname);
        LOG.info("The oldlastname: "+oldlastname);
        LOG.info("The firstname: "+firstname);
        LOG.info("The lastname: "+lastname);
        
        if (!personService.getByFirstAndLastName(firstname, lastname).isEmpty()
                && (!oldfirstname.equals(firstname) || !oldlastname.equals(lastname))){
            return new ModelAndView("person/search-person-miss-page")
                    .addObject("message", "Person with such firstname and lastname is already exists!");
        }
        
        Person pnew = personService.editPerson(oldfirstname, oldlastname, firstname, lastname, age, gender);
        LOG.info("Try to add a new film set: "+lastname);
        if (movieIds == null) movieIds = new ArrayList<>();
        personService.addMoviesToPerson(pnew, movieIds);
        LOG.info("addMoviesToPerson successfull!: "+lastname);
        
        ModelAndView mv = new ModelAndView("person/person-info");
        mv.addObject("person", personService.getByFirstAndLastName(firstname, lastname).get(0));
        mv.addObject("movies", personService.getByFirstAndLastName(firstname, lastname).get(0).getMovies());
        
        return mv;
    }
    
    @PostMapping(value = "/delete-person")
    ModelAndView deletePerson(@RequestParam(value="firstname") String firstname,
            @RequestParam(value="lastname") String lastname)
    {
        LOG.info("Delete person post controller: ");
        LOG.info("fname: "+firstname);
        LOG.info("lname: "+lastname);
        
        personService.deleteByFirstAndLastName(firstname, lastname);
        
        return new ModelAndView("person/all-persons").addObject("persons", personService.getAll());
    }
    
    //===========Create Person=====================
    
    @GetMapping(value = "/create-person-page")
    ModelAndView createPersonPage(){
        ModelAndView mv = new ModelAndView("person/create-person-page");
        mv.addObject("movies", movieService.getAll()); //add movies for binding them to a new person
        return mv;
    }
    
    @PostMapping(value = "/create-person-page")
    ModelAndView createPerson(@RequestParam(value="firstname") String firstname,
            @RequestParam(value="lastname") String lastname,
            @RequestParam(value="age") Integer age,
            @RequestParam(value="gender") Gender gender,
            @RequestParam(value="movieIds", required = false) List<Long> movieIds)
    {
        LOG.info("Внутри create person post controller: ");
        LOG.info("fname: "+firstname);
        LOG.info("lname: "+lastname);
        LOG.info("age: "+age);
        LOG.info("gender: "+gender);
        LOG.info("Films: ");
        if (movieIds == null) movieIds = new ArrayList<>();
        for(Long id : movieIds){
            LOG.info("Id: "+id);
        }
        
        if (!personService.getByFirstAndLastName(firstname, lastname).isEmpty()){
            return new ModelAndView("person/create-person-miss-page").addObject("message", "Person with such firstname and lasname is already exists!");
        }
        
        Person person = new Person(firstname, lastname, age, gender);
        
        personService.addPersonWithMovies(person, movieIds);
        //...
        
        LOG.info("After saving person has become person with movies: ");
        for(Movie movie : personService.getByFirstAndLastName(firstname, lastname).get(0).getMovies()){
            LOG.info("Film: "+movie.getTitle());
        }
        
        return new ModelAndView("person/all-persons").addObject("persons", personService.getAll());
    }
    
    @GetMapping(value = "/create-person-miss-page/{message}")
    ModelAndView createPersonMissPage(@PathVariable(value = "message") String message){
        ModelAndView mv = new ModelAndView("person/create-person-miss-page");
        return mv;
    }
    
}
