package WebApplication.WebTour.Controllers.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller("ss")
public class ProfileController {

	@GetMapping("/account/profile")
	public String showPageProfile(Model model) {
		return "/User/profile";
	}
	
	@GetMapping("/account/change-password")
	public String showPageChangePassword(Model model) {
		return "/User/change-password";
	}
	
}
