package WebApplication.WebTour.Controllers.User;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

@Controller
public class OrderController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	BookingsRespository bookingsRepository;

	// hiện trang order theo userId trên url
	@GetMapping("/account/get-order")
	public String showOrderPage(@RequestParam(value = "userId", required = false) Long userId, Model model) {
		Optional<User> user = userRepository.findById(userId);
		model.addAttribute("user", user.get());
		List<Object[]> bookings = bookingsRepository.showDataTable(userId);
		System.out.println("bookings " + bookings);
		
//		for (Object[] booking : bookings) {
//	        System.out.println("Booking details: ");
//	        for (Object field : booking) {
//	            System.out.print(field + " ");  
//	        }
//	        System.out.println();  
//		}
		
		model.addAttribute("bookings", bookings);

		return "/User/order";
	}
	
	//lọc booking paid hoặc unpaid
	@GetMapping("/account/filter-get-order")
	@ResponseBody 
	public List<Object[]> filterOrderPage(
	        @RequestParam(value = "userId", required = false) Long userId,
	        @RequestParam(value = "paymentStatus", required = false) Integer paymentStatus) {

	    List<Object[]> bookings = bookingsRepository.filterOrderPage(userId, paymentStatus);
	    return bookings; // Trả về dữ liệu bookings dưới dạng JSON
	}
	
	//tìm kiếm departure
	@GetMapping("/account/search-departure")
	@ResponseBody
	public List<Object[]> searchDeparture(
	        @RequestParam(value = "userId", required = false) Long userId,
	        @RequestParam(value = "searchInput", required = false) String searchInput) {

	    List<Object[]> results = bookingsRepository.searchDeparture(userId, searchInput);
	    return results; 
	}


	
}
