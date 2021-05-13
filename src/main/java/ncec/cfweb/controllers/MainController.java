package ncec.cfweb.controllers;

import java.util.List;
import ncec.cfweb.Movie;
import ncec.cfweb.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author DantalioNxxi
 */
@Controller
@RequestMapping("/")
public class MainController {
    
//    @Autowired
//    MovieService movieService;
    
    @GetMapping()
    ModelAndView index(){
        return new ModelAndView("index");
    }
    
}
