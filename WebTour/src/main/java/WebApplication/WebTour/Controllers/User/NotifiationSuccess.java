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
	
	
	
	// chuyển đến trang thanh toán thành công và hiển thị các thông tin của khách
	// hàng
	@GetMapping("/notificationSuccess/{bookingid}")
	public String openNotificationSuccessForm(@PathVariable("bookingid") Long bookingid, Model model) {

		Optional<Bookings> bookingOpt = bookingsRespository.findById(bookingid);
		if (!bookingOpt.isPresent()) {
			return "bookingOpt notificationSuccess không tồn tại!";
		}
		Bookings booking = bookingOpt.get();

		// Tìm user theo userId của booking
		User userOpt =  booking.getUser();
		if (userOpt==null) {
			return "userOpt notificationSuccess không tồn tại!";
		}
		

		// Tìm payment theo paymentId của booking
		Payments paymentOpt = booking.getPayment();
		if (paymentOpt==null) {
			return "paymentOpt notificationSuccess không tồn tại!";
		}
		

		// Tìm tour theo paymentId của booking
		Tours tourtOpt = booking.getTour();
		if (tourtOpt==null) {
			return "tourtOpt notificationSuccess không tồn tại!";
		}
		
		
		//hiển thị số lượng người lớn và trẻ em
		List<TicketBooking> ticketBookingList = ticketBookingRepository.findTicketBookingById(booking.getBookingId());
		if(ticketBookingList.isEmpty()) {
			return "ticketBookingList notificationSuccess không tồn tại!";
		}
		
		
		
		
		// Thêm thông tin vào model để hiển thị trên trang notificationSuccess
		model.addAttribute("bookingNotification", booking);
		model.addAttribute("paymentNotification", paymentOpt);
		model.addAttribute("userNotification", userOpt);
		model.addAttribute("tourNotification", tourtOpt);
		model.addAttribute("ticketBookingList", ticketBookingList);
		return "/User/notificationSuccess";
	}

}
