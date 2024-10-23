package WebApplication.WebTour.Controllers.User;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import WebApplication.WebTour.Model.Promotions;

@Controller
public class PromotionProgramController {
	@GetMapping("/promotion-program")
	public String customerManagementPage(Model model) {
		return "/User/promotion_program";
	}
}
