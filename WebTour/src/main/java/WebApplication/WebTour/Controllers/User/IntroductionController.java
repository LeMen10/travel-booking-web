package WebApplication.WebTour.Controllers.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IntroductionController {
	@GetMapping("/insurance-tour")
    public String showInsuranceTourPage(Model model) {
            return "/User/insurance-tour";
    }
    
    @GetMapping("/visa")
    public String showVisaPage(Model model) {
            return "/User/visa";
    }
}
