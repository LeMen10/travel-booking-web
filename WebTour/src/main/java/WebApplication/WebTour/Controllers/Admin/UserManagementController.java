package WebApplication.WebTour.Controllers.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	private UserService userService;

	@GetMapping("/admin/customer-management")
	public String customerManagementPage(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "4") int size, Model model) {
		Page<Object[]> users = userService.getUsersByRoleId(3, PageRequest.of(page, size));

		model.addAttribute("users", users.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", users.getTotalPages());
		return "/Admin/customer_management";
	}

	@GetMapping("/admin/bookings-count")
	@ResponseBody
	public List<Object[]> getUsersBookingCounts(@RequestParam(defaultValue = "asc") String sort) {
		return userService.getUsersOrderedByBookings(sort);
	}
}
