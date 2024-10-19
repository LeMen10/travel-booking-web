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
		// lấy thông tin booking từ bookingsRespository
		Optional<Bookings> bookingOpt = bookingsRespository.findById(bookingId);
		if (bookingOpt.isPresent()) {
			Bookings booking = bookingOpt.get();
			System.out.println("User ID from booking: " + booking.getUserId());

			// Lấy thông tin user từ userId trong booking
			Optional<User> user = userRepository.findById((long) booking.getUserId());
			if (user.isPresent()) {
				model.addAttribute("user", user.get());
			} else {
				System.out.println("User không tồn tại!");
	            model.addAttribute("user", null);  // Đảm bảo user là null nếu không tìm thấy
	            model.addAttribute("error", "User không tồn tại!");
			}

			Optional<Bookings> bookingPayment = bookingsRespository.findById(bookingId);
			if (bookingPayment.isPresent()) {
				model.addAttribute("bookingPayment", bookingPayment.get());
			} else {
				model.addAttribute("error", "bookingPayment không tồn tại!");
			}

			// Lấy thông tin payment
			Optional<Payments> payment = paymentsRepository.findById(bookingId);
			if (payment.isPresent()) {
				model.addAttribute("payment", payment.get());
			} else {
				model.addAttribute("error", "Payment không tồn tại!");
			}
			// lấy thông tin của tour
			Optional<Tours> tourPayment = toursRepository.findById((long) booking.getTourId());
			if (tourPayment.isPresent()) {
				model.addAttribute("tourPayment", tourPayment.get());
			} else {
				model.addAttribute("error", "TourPayment không tồn tại!");
			}
			// lấy ticketBooking để hiển thị số lượng người lớn và trẻ em
			List<TicketBooking> ticketBooking = ticketBookingRepository.findTicketBookingById(booking.getBookingId());
			if (!ticketBooking.isEmpty()) {
				model.addAttribute("ticketBookings", ticketBooking);
				System.out.println(ticketBooking);
			} else {
				model.addAttribute("error", "ticketBooking không tồn tại!");
			}

		} else {
			model.addAttribute("error", "Booking không tồn tại!");
		}

		return "/User/notificationSuccess";
	}

}
