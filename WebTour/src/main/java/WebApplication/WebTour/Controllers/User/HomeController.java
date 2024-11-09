package WebApplication.WebTour.Controllers.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.DTO.AccountUserDTO;
import WebApplication.WebTour.DTO.EmailRequest;
import WebApplication.WebTour.Model.Account;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Service.HomeService;
import WebApplication.WebTour.Respository.ProvinceRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {
	
	@Autowired
	ProvinceRepository provinceRepository;
	
	@Autowired
	ToursRepository toursRepository;
	
	@Autowired
	private HomeService homeService;
    
    @GetMapping("/api-get-header")
    public String getHeaderHTML(Model model) {
    		return "/components/header";
    }
    
    @GetMapping("/api-get-footer")
    public String getFooterHTML(Model model) {
    		return "/components/footer";
    }
    
    @GetMapping("/api-get-header-employee")
    public String getHeaderEmployeeHTML(Model model) {
    		return "/components/headerEmployee";
    }
    
    @GetMapping("/api-get-notification")
    public String getNotificationHTML(Model model) {
    		return "/components/notification";
    }
    
	@GetMapping("/home")
    public String navigateHomePage(Model model) {
			model.addAttribute("province",provinceRepository.findAll());
			model.addAttribute("tours", toursRepository.listOfCheapestTours());
            return "home";
    }
	
    @GetMapping("/")
    public String navigateLoginPage(Model model) {
            return "login";
    }

    @GetMapping("/forgot-password")
    public String navigateForgotPassworPage(Model model) {
            return "forgotPassword";
    }
    
    @GetMapping("/get-account/{username}")
    public ResponseEntity<?> getAccountByUserName(@PathVariable String username) {
    	Account newAccount = homeService.getAccountByUserName(username);
        
        if (newAccount != null) {
            return ResponseEntity.ok(newAccount);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/add-account")
    public ResponseEntity<?> addAccoutUser(@RequestBody  AccountUserDTO userDTO) {
        
    	Account newAccount = homeService.addAccoutUser(userDTO);
        if (newAccount != null) {
            return ResponseEntity.ok(newAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/api-update-new-password")
    public ResponseEntity<?> updatePassword(@RequestParam Long accountId, @RequestParam String newPassword) {
    	boolean isSuccess = homeService.updateNewPassword(accountId, newPassword);
    	if(isSuccess) return ResponseEntity.ok("Update successfully!");
    	else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/send-mail")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
    	boolean isSuccess = homeService.sendEmail(emailRequest);
    	if(isSuccess) return ResponseEntity.ok("Email sent successfully!");
    	else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/employee")
    public String navigateEmployeeHomePage(Model model) {
            return "/Employee/home";
    }
    
}
