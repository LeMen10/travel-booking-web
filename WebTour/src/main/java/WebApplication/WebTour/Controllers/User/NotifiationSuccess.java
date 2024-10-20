package WebApplication.WebTour.Controllers.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Model.Payments;
import WebApplication.WebTour.Model.TicketBooking;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.BookingsRespository;
import WebApplication.WebTour.Respository.PaymentsRepository;
import WebApplication.WebTour.Respository.TicketBookingRepository;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Respository.UserRepository;

@Controller
public class NotifiationSuccess {
	@Autowired
	BookingsRespository bookingsRespository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	TicketBookingRepository ticketBookingRepository;
	@Autowired
	PaymentsRepository paymentsRepository;
	@Autowired
	ToursRepository toursRepository;

	// chuyển đến trang card (các tour đã đặt khi thanh toán thành công)
	@GetMapping("/notificationSuccess/{bookingid}")
	public String openNotificationSuccessForm(@PathVariable("bookingid") Long bookingid, Model model) {
		Optional<Bookings> booking = bookingsRespository.findById(bookingid);
		if (booking.isPresent()) {
			model.addAttribute("booking", booking.get());
		} else {
			model.addAttribute("error", "booking không tồn tại!");
		}
		return "/User/notificationSuccess";
	}

	// lấy thông tin trong thông báo thanh toán thành công
	@GetMapping("/api-success/{bookingId}")
	public String notificationSuccess(@PathVariable("bookingId") Long bookingId, Model model) {
		
		Bookings b = bookingsRespository.findById(bookingId).get();
		User u = b.getUser();
		System.out.println(u);
		model.addAttribute("user", u);
		return "/User/notificationSuccess";
	}

}
