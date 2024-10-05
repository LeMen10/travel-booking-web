package WebApplication.WebTour.Controllers.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.Customer;
import WebApplication.WebTour.Respository.CustomerRespository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller  // Use @Controller for returning views
public class HomeController {

    @Autowired
    CustomerRespository customerRespository;

    @GetMapping("/")
    public String navigateHomePage(Model model) {

            return "login";
    }

    @GetMapping("/customer")
    public ResponseEntity<?> listAllCustomers() {
        List<Customer> listCustomer = customerRespository.findAll();
        if (listCustomer.isEmpty()) {
            return new ResponseEntity<>("No customers found", HttpStatus.OK);  // Return a message if the list is empty
        }
        return new ResponseEntity<>(listCustomer, HttpStatus.OK);
    }

    @GetMapping("/home")
    public String navigateLoginPage(Model model) {
        return "home";  // Return the name of the view without .html
    }
}
