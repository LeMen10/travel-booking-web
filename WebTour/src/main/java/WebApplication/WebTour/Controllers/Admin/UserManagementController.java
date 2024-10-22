package WebApplication.WebTour.Controllers.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.UserRepository;
import WebApplication.WebTour.Service.UserService;

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
    
    @Autowired
    private UserService userService;

    @GetMapping("/admin/bookings-count")
    @ResponseBody
    public List<Object[]> getUsersBookingCounts(@RequestParam(defaultValue = "asc") String sort) {
        return userService.getUsersOrderedByBookings(sort);
    }
}
