package WebApplication.WebTour.Controllers.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Respository.ToursRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller  // Use @Controller for returning views
public class HomeController {

//    @Autowired
    ToursRepository toursRepository;
    
    @GetMapping("/header")
    public String nagivateHomePage(Model model) {
    	return "/components/header";
    }
}
