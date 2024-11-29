package WebApplication.WebTour.Controllers.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Model.Guides;
import WebApplication.WebTour.Model.Payments;
import WebApplication.WebTour.Model.Promotions;
import WebApplication.WebTour.Model.TicketBooking;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.BookingsRespository;
import WebApplication.WebTour.Respository.GuidesRepository;
import WebApplication.WebTour.Respository.PaymentsRepository;
import WebApplication.WebTour.Respository.PromotionsRepository;
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
	@Autowired
	PromotionsRepository promotionsRepository;
	@Autowired
	GuidesRepository guidesRepository;

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
		User userOpt = booking.getUser();
		if (userOpt == null) {
			return "userOpt notificationSuccess không tồn tại!";
		}

		// Tìm payment theo paymentId của booking
		Payments paymentOpt = booking.getPayment();
		if (paymentOpt == null) {
			System.out.println("Payment không tồn tại cho booking: " + booking.getBookingId());
			return "paymentOpt notificationSuccess không tồn tại!";
		}
		// Tìm promotion theo promotionCode (hiện mã giảm ở trang thông báo thanh toán
		Optional<Promotions> promotionOpt = promotionsRepository.findByCode(paymentOpt.getPromotionCode());
		if (promotionOpt.isPresent()) {
			Promotions promotion = promotionOpt.get();
			System.out.println("Promotion found: " + promotion.getDiscount() + "%");
			model.addAttribute("promotionNotification", promotion);
		} else {
			System.out.println("Promotion không tồn tại với mã: ");
			model.addAttribute("promotionNotification", null);
		}
		// Tìm promotion theo promotionCode (hiện mã giảm ở trang thông báo thanh toán
		// success)
		/*
		 * Optional<Promotions> promotionOpt =
		 * promotionsRepository.findByCode(paymentOpt.getPromotionCode()); if
		 * (promotionOpt.isEmpty()) { return "Promotion không tồn tại!"; } Promotions
		 * promotion = promotionOpt.get();
		 */

		// Tìm tour theo paymentId của booking
		Tours tourtOpt = booking.getTour();
		if (tourtOpt == null) {
			return "tourtOpt notificationSuccess không tồn tại!";
		}

		// lấy hướng dẫn viên từ tour
		Guides guide = tourtOpt.getGuides();
		if (guide == null) {
			model.addAttribute("guidesNotification", "Không có hướng dẫn viên");
		} else {
			model.addAttribute("guidesNotification", guide);
			System.out.println(guide.getGuideId());
		}

		// hiển thị số lượng người lớn và trẻ em
		List<TicketBooking> ticketBookingList = ticketBookingRepository.findTicketBookingById(booking.getBookingId());
		if (ticketBookingList.isEmpty()) {
			return "ticketBookingList notificationSuccess không tồn tại!";
		}

		// Thêm thông tin vào model để hiển thị trên trang notificationSuccess
		model.addAttribute("bookingNotification", booking);
		model.addAttribute("paymentNotification", paymentOpt);
		// model.addAttribute("promotionNotification", promotion);
		// System.out.println("promotionNotification " + promotion.getCode());
		model.addAttribute("userNotification", userOpt);
		model.addAttribute("tourNotification", tourtOpt);
		model.addAttribute("ticketBookingList", ticketBookingList);
		return "/User/notificationSuccess";
	}
	
	@GetMapping("/notificationSuccess-momo")
	public String openNotificationSuccessFormM0M0(@RequestParam("bookingId") Long bookingId, 
			@RequestParam("orderId") String orderId, 
			@RequestParam("resultCode") int resultCode,
			Model model) {

		//momo
		model.addAttribute("resultCode", resultCode);
		model.addAttribute("bookingId", bookingId);
		model.addAttribute("orderId", orderId);
		System.out.println(orderId);
		return "/User/notificationSuccessMoMo";
	}

}
