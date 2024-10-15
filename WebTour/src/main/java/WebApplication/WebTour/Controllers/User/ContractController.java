package WebApplication.WebTour.Controllers.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContractController {
	
	@GetMapping("/contract")
	public String ShowContract(Model model) {
		return "/User/contract";
	}
}
