package WebApplication.WebTour.Controllers.User;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Pageable;

import WebApplication.WebTour.Model.Address;
import WebApplication.WebTour.Model.District;
import WebApplication.WebTour.Model.Image;
import WebApplication.WebTour.Model.Province;
import WebApplication.WebTour.Model.Reviews;
import WebApplication.WebTour.Model.Schedules;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Model.Ward;
import WebApplication.WebTour.Respository.BookingsRespository;
import WebApplication.WebTour.Respository.UserRepository;
import WebApplication.WebTour.Service.BookingService;
import WebApplication.WebTour.Service.OrderService;

@Controller
public class OrderController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	BookingsRespository bookingsRepository;
	@Autowired
	OrderService orderService;

	// hiện trang order theo userId trên url
	@GetMapping("/account/get-order")
	public String showOrderPage(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "5") int size, Model model) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			model.addAttribute("user", user.get());

			Page<Object[]> bookingsPage = orderService.ShowDataTable(userId, PageRequest.of(page, size));
			/*
			 * for (Object[] booking : bookingsPage.getContent()) {
			 * System.out.println("Payment ID: " + booking[10]);
			 * System.out.println("Thanh toán: " + booking[2]); }
			 */

			model.addAttribute("bookings", bookingsPage.getContent());
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", bookingsPage.getTotalPages());
		} else {
			model.addAttribute("errorMessage", "User không tồn tại.");
		}

		return "/User/order";

	}

	// lọc booking paid hoặc unpaid
	@GetMapping("/account/filter-get-order")
	@ResponseBody
	public Page<Object[]> filterOrderPage(
	        @RequestParam(value = "userId", required = false) Long userId,
	        @RequestParam(value = "paymentStatus", required = false) Integer paymentStatus,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "5") int size) {

	    Pageable pageable = PageRequest.of(page, size);
	    return orderService.FilterOrderPage(userId, paymentStatus, pageable);
	}

	/*
	 * @GetMapping("/account/filter-get-order")
	 * 
	 * @ResponseBody public List<Object[]> filterOrderPage(@RequestParam(value =
	 * "userId", required = false) Long userId,
	 * 
	 * @RequestParam(value = "paymentStatus", required = false) Integer
	 * paymentStatus) {
	 * 
	 * List<Object[]> bookings = bookingsRepository.filterOrderPage(userId,
	 * paymentStatus); return bookings; }
	 */

	// tìm kiếm departure
	@GetMapping("/account/search-departure")
	@ResponseBody
	public Page<Object[]> searchDeparture(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "searchInput", required = false) String searchInput,
			@RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "5") int size) {

		Pageable pageable = PageRequest.of(page, size);
		return orderService.SearchDeparture(userId, searchInput ,pageable);

	}
	/*
	 * @GetMapping("/account/search-departure")
	 * 
	 * @ResponseBody public List<Object[]> searchDeparture(@RequestParam(value =
	 * "userId", required = false) Long userId,
	 * 
	 * @RequestParam(value = "searchInput", required = false) String searchInput) {
	 * 
	 * List<Object[]> results = bookingsRepository.searchDeparture(userId,
	 * searchInput); return results; }
	 */

}
