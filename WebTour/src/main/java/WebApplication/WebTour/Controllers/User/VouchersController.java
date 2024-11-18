package WebApplication.WebTour.Controllers.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.Promotions;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.UserRepository;
import WebApplication.WebTour.Service.VouchersService;

@Controller
public class VouchersController {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	VouchersService vouchersService;
	
	@GetMapping("/account/get-vouchers")
	public String showVoucherPage(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size, Model model) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			model.addAttribute("user", user.get());

			Page<Promotions> data = vouchersService.showDataTable(userId, PageRequest.of(page, size));
			for (Promotions voucher : data.getContent()) {
	            System.out.println("Voucher ID: " + voucher.getPromotionId());
	        }

			model.addAttribute("vouchers", data.getContent());
		} else {
			model.addAttribute("errorMessage", "User không tồn tại.");
		}

		return "/User/vouchers";
	}
}
