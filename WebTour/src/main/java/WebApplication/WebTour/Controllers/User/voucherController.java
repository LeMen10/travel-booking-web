package WebApplication.WebTour.Controllers.User;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import WebApplication.WebTour.Model.Promotions;

@Controller
public class voucherController {
	@GetMapping("/vouchers")
	public String voucherPage(Model model) {
		return "/User/vouchers";
	}
}
