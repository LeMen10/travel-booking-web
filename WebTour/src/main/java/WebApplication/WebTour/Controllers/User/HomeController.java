package WebApplication.WebTour.Controllers.User;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.DTO.AccountUserDTO;
import WebApplication.WebTour.DTO.EmailRequest;
import WebApplication.WebTour.Model.Account;
import WebApplication.WebTour.Respository.AccountRespository;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Respository.UserRepository;
import WebApplication.WebTour.Model.Province;
import WebApplication.WebTour.Model.Role;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.ProvinceRepository;
import WebApplication.WebTour.Respository.RoleRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {

	@Autowired
	AccountRespository accountRespository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProvinceRepository provinceRepository;
	
	@Autowired
	ToursRepository toursRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
    @Autowired
    private JavaMailSender mailSender;
	
    
    @GetMapping("/api-get-header")
    public String getHeaderHTML(Model model) {
    		return "/components/header";
    }
	@GetMapping("/home")
    public String navigateHomePage(Model model) {
			List<Province> province = provinceRepository.findAll();
			List<Tours> tours = toursRepository.listOfCheapestTours();
			model.addAttribute(province);
			model.addAttribute(tours);
			for (Tours tours2 : tours) {
				System.out.println(tours2);
			}
			if(tours.isEmpty())
			{
				System.out.println("khum cos gif luoon");
			}
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
        System.out.println(username);
        Optional<Account> account = accountRespository.findByUserName(username);
        
        if (account != null && account.isPresent()) {
            System.out.println(account.get().toString());
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/add-account")
    public ResponseEntity<?> addAccoutUser(@RequestBody  AccountUserDTO userDTO) {
        
    	System.out.println(userDTO);
        if (userDTO != null) {
        	Role role = roleRepository.findById(3l).get();
        	if(role == null) return ResponseEntity.notFound().build();
        	User user = new User(role, userDTO.getFullName(), userDTO.getEmail(),userDTO.getPhone(), userDTO.getGender(), null);
        	User userCreated = userRepository.save(user);
        	Account accountCreated = accountRespository.save(new Account(userCreated, userDTO.getUserName(), userDTO.getPassword()));
            return accountCreated == null ? ResponseEntity.notFound().build(): ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/send-mail")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
    	System.out.println(emailRequest);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailRequest.getTo());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getBody());
        mailSender.send(message);
        return ResponseEntity.ok("Email sent successfully!");
    }
    
    @GetMapping("/admin/")
    public String navigateAdminHomePage(Model model) {
            return "/Admin/home";
    }
    
}
