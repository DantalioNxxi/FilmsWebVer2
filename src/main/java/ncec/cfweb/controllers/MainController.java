package ncec.cfweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
