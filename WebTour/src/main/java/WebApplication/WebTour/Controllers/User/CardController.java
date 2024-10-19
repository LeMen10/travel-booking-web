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
import WebApplication.WebTour.Respository.TicketBookingRepository;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Respository.UserRepository;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CardController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	BookingsRespository bookingsRespository;
	@Autowired
	TicketBookingRepository ticketBookingRepository;
	@Autowired
	ToursRepository toursRepository;

	// chuyển đến trang card (các tour đã đặt khi thanh toán thành công)
	@GetMapping("/card/{userId}")
	public String openPaymentForm(@PathVariable("userId") Long userId, Model model) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			model.addAttribute("user", user.get());
		} else {
			model.addAttribute("error", "User không tồn tại!");
		}
		return "/User/card";
	}

	// hiển thị các tour đã đặt lên trang card
	@GetMapping("api-show-card/{userId}")
	public String showCard(@PathVariable("userId") Long userId, Model model) {
		Optional<User> userOpt = userRepository.findById(userId);

		if (userOpt.isPresent()) {
			User user = userOpt.get();

			Optional<Bookings> bookingOpt = bookingsRespository.findById(user.getUser_id());
			if (bookingOpt.isPresent()) {
				Bookings booking = bookingOpt.get();
				// lấy thông tin của user
				Optional<User> userCard = userRepository.findById((long) user.getUser_id());
				if (userCard.isPresent()) {
					model.addAttribute("userCard", userCard.get());
				} else {
					model.addAttribute("error", "User không tồn tại!");
				}

				// lấy thông tin của tour
				Optional<Tours> tourPayment = toursRepository.findById((long) booking.getTourId());
				if (tourPayment.isPresent()) {
					model.addAttribute("tourPayment", tourPayment.get());
				} else {
					model.addAttribute("error", "TourPayment không tồn tại!");
				}

				// lấy thông tin của booking
				Optional<Bookings> bookingPayment = bookingsRespository.findById((long) user.getUser_id());
				if (bookingPayment.isPresent()) {
					model.addAttribute("bookingPayment", bookingPayment.get());
				} else {
					model.addAttribute("error", "bookingPayment không tồn tại!");
				}

				// lấy ticketBooking để hiển thị số lượng người lớn và trẻ em
				List<TicketBooking> ticketBooking = ticketBookingRepository
						.findTicketBookingById(booking.getBookingId());
				if (!ticketBooking.isEmpty()) {
					model.addAttribute("ticketBookings", ticketBooking);
					System.out.println(ticketBooking);
				} else {
					model.addAttribute("error", "ticketBooking không tồn tại!");
				}

			} else {
				model.addAttribute("error", "booking không tồn tại!");
			}

		} else {
			model.addAttribute("error", "User không tồn tại!");
		}

		return "/User/card";
	}

}
