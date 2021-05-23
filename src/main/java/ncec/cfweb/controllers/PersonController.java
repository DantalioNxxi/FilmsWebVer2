package ncec.cfweb.controllers;

import lombok.extern.slf4j.Slf4j;
import ncec.cfweb.entity.Person;
import ncec.cfweb.services.MovieService;
import ncec.cfweb.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author DantalioNxxi
 */
@Slf4j
@RequestMapping("/person")
@Controller
public class PersonController {
    
    @Autowired
    PersonService personService;
    
    @Autowired
    MovieService movieService;
    
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
        final Person person = personService.getByFirstAndLastName(firstname, lastname).get(0);
        mv.addObject("person", person);
        mv.addObject("movies", movieService.getByIds(person.getMovies()));
        return mv;
    }
    
    //===========Search Persons=====================
    
    @RequestMapping(value = "/search-person-page", method = RequestMethod.POST)
    String searchPerson(Model model, @RequestParam String fullName, RedirectAttributes redirectAttributes){
        log.info("Searching movie with name "+ fullName+ "...");
        redirectAttributes.addAttribute("fullName", fullName);
        
        List<Person> persons = personService.getAll();
        if (!persons.isEmpty()){
            for(Person p : persons){
                if(p.getFirstname().concat(" ").concat(p.getLastname()).contains(fullName)){
                    log.info("Such person with name "+ fullName+ " was founded");
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
    ModelAndView postEditPerson(//НАДО ИСПРАВИТЬ ЭТОТ МЕТОД;
            @RequestParam(value="oldfirstname") String oldfirstname,
            @RequestParam(value="oldlastname") String oldlastname,
            @RequestParam(value="firstname") String firstname,
            @RequestParam(value="lastname") String lastname,
            @RequestParam(value="age") int age,
            @RequestParam(value="genderId") UUID genderId,
            @RequestParam(value="movieIds", required = false) List<UUID> movieIds)
    {
        
        log.info("The oldfirstname: "+oldfirstname);
        log.info("The oldlastname: "+oldlastname);
        log.info("The firstname: "+firstname);
        log.info("The lastname: "+lastname);
        
        if (!personService.getByFirstAndLastName(firstname, lastname).isEmpty()
                && (!oldfirstname.equals(firstname) || !oldlastname.equals(lastname))){
            return new ModelAndView("person/search-person-miss-page")
                    .addObject("message", "Person with such firstname and lastname is already exists!");
        }
        
        Person pnew = personService.editPerson(oldfirstname, oldlastname, firstname, lastname, age, genderId);
        log.info("Try to add a new film set: "+lastname);
        if (movieIds == null) movieIds = new ArrayList<>();
        personService.addMoviesToPerson(pnew, movieIds);
        log.info("addMoviesToPerson successfull!: "+lastname);
        
        ModelAndView mv = new ModelAndView("person/person-info");
        mv.addObject("person", personService.getByFirstAndLastName(firstname, lastname).get(0));
        mv.addObject("movies", personService.getByFirstAndLastName(firstname, lastname).get(0).getMovies());
        
        return mv;
    }
    
    @PostMapping(value = "/delete-person")
    ModelAndView deletePerson(@RequestParam(value="firstname") String firstname,
            @RequestParam(value="lastname") String lastname)
    {
        log.info("Delete person post controller: ");
        log.info("fname: "+firstname);
        log.info("lname: "+lastname);
        
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
            @RequestParam(value="genderId") UUID genderId,
            @RequestParam(value="movieIds", required = false) List<UUID> movieIds)
    {
        log.info("Внутри create person post controller: ");
        log.info("fname: "+firstname);
        log.info("lname: "+lastname);
        log.info("age: "+age);
        log.info("genderId: "+genderId);
        log.info("Films: ");
        if (movieIds == null) movieIds = new ArrayList<>();
        for(UUID id : movieIds){
            log.info("Id: "+id);
        }
        
        if (!personService.getByFirstAndLastName(firstname, lastname).isEmpty()){
            return new ModelAndView("person/create-person-miss-page").addObject("message", "Person with such firstname and lasname is already exists!");
        }
        
        Person person = new Person(firstname, lastname, age, genderId);
        
        personService.addPersonWithMovies(person, movieIds);
        //...
        
//        log.info("After saving person has become person with movies: ");
//        for(Movie movie : personService.getByFirstAndLastName(firstname, lastname).get(0).getMovies()){
//            log.info("Film: "+movie.getTitle());
//        }
        
        return new ModelAndView("person/all-persons").addObject("persons", personService.getAll());
    }
    
    @GetMapping(value = "/create-person-miss-page/{message}")
    ModelAndView createPersonMissPage(@PathVariable(value = "message") String message){
        ModelAndView mv = new ModelAndView("person/create-person-miss-page");
        return mv;
    }
    
}
