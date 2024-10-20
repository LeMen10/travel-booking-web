package WebApplication.WebTour.Controllers.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.UserRepository;

@Controller
public class UserManagementController {
	@Autowired
    UserRepository userRepository;

    @GetMapping("/admin/customer-management")
    public String customerManagementPage(Model model) {
    	 	List<User> users = userRepository.findByRoleId(3);
    	 	
    	 	model.addAttribute("users", users);
    		return "/Admin/customer_management";
    }
}
