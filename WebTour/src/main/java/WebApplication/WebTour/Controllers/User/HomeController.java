package WebApplication.WebTour.Controllers.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import WebApplication.WebTour.Model.Account;
import WebApplication.WebTour.Respository.AccountRespository;
import WebApplication.WebTour.Respository.ToursRepository;

import WebApplication.WebTour.Model.Province;
import WebApplication.WebTour.Respository.ProvinceRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {
	@Autowired
	private ProvinceRepository provinceRepository;


	@Autowired
	AccountRespository accountRespository;
	
	@GetMapping("/home")
    public String navigateHomePage(Model model) {
            return "home";
    }
	
    @GetMapping("/")
    public String navigateLoginPage(Model model) {
            return "login";
    }

    @GetMapping("/get-account/{username}")
    public ResponseEntity<?> getAccountByUserName(@PathVariable String username) {
        System.out.println(username);
        Optional<Account> account = accountRespository.findByUserName(username);
        
        if (account.isPresent()) {
            System.out.println(account.get().toString());
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
}
