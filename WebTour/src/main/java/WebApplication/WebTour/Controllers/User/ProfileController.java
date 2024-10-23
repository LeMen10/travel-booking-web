package WebApplication.WebTour.Controllers.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import WebApplication.WebTour.Model.Account;
import WebApplication.WebTour.Model.Address;
import WebApplication.WebTour.Model.District;
import WebApplication.WebTour.Model.Province;
import WebApplication.WebTour.Model.Ticket;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Model.Ward;
import WebApplication.WebTour.Respository.UserRepository;
import jakarta.servlet.http.HttpSession;


@Controller("sss")
public class ProfileController {
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/account")
	public String showPageProfile( @RequestParam(value = "userId", required = false) Long userId,Model model) {
		
		Optional<User> user = userRepository.findById(userId);
		Address address = user.get().getAddress();
		Province province = address.getProvince();
		Ward ward = address.getWard();
		District district = address.getDistrict();
		
		model.addAttribute("user", user.get());
		model.addAttribute("address", address);
		model.addAttribute("province", province);
		model.addAttribute("ward", ward);
		model.addAttribute("district", district);
        return "/User/profile";
	}
	
	@GetMapping("/account/change-password")
	public String showPageChangePassword(Model model) {
		return "/User/change-password";
	}
	
	@GetMapping("/api-get-user") // để sử dụng cho dấu cộng, trừ số lượng vé (trẻ em hay người lớn)
	public ResponseEntity<User> GetUser(@RequestParam("userId") Long userId, Model model) {
		Optional<User> user = userRepository.findById(userId);
		// Kiểm tra nếu có giá trị trong Optional
		
		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		
	}
}
