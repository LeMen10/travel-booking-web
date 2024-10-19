package WebApplication.WebTour.Controllers.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import WebApplication.WebTour.Model.Address;
import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Model.District;
import WebApplication.WebTour.Model.Payments;
import WebApplication.WebTour.Model.Promotions;
import WebApplication.WebTour.Model.Province;
import WebApplication.WebTour.Model.Ticket;
import WebApplication.WebTour.Model.TicketBooking;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Model.Ward;
import WebApplication.WebTour.Respository.AddressRespository;
import WebApplication.WebTour.Respository.BookingsRespository;
import WebApplication.WebTour.Respository.DistrictRespository;
import WebApplication.WebTour.Respository.PaymentsRepository;
import WebApplication.WebTour.Respository.PromotionsRepository;
import WebApplication.WebTour.Respository.ProvinceRepository;
import WebApplication.WebTour.Respository.TicketBookingRepository;
import WebApplication.WebTour.Respository.TicketRepository;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Respository.UserRepository;
import WebApplication.WebTour.Respository.WardRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class PaymentController {
	@Autowired
	PaymentsRepository paymentsRepository;
	@Autowired
	BookingsRespository bookingsRespository;
	@Autowired
	ProvinceRepository provinceRepository;
	@Autowired
	DistrictRespository districtRespository;
	@Autowired
	WardRepository wardRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PromotionsRepository promotionsRepository;
	@Autowired
	ToursRepository toursRepository;
	@Autowired
	AddressRespository addressRespository;
	@Autowired
	TicketBookingRepository ticketBookingRepository;

	// lấy id booking (có user trong đó để hiện thị t/tin user lên trang) vừa tạo và
	// mở trang payment
	@GetMapping("/payment/{bookingId}")
	public String openPaymentForm(@PathVariable("bookingId") Long bookingId, Model model) {
		// llấy thông tin booking từ bookingsRespository
		Optional<Bookings> bookingOpt = bookingsRespository.findById(bookingId);
		if (bookingOpt.isPresent()) {
			Bookings booking = bookingOpt.get();
			
			Optional<Bookings> bookingPayment = bookingsRespository.findById(bookingId);
			if (bookingPayment.isPresent()) {
				model.addAttribute("bookingPayment", bookingPayment.get());
			} else {
				model.addAttribute("error", "bookingPayment không tồn tại!");
			}
			// Lấy thông tin user từ userId trong booking
			Optional<User> user = userRepository.findById((long) booking.getUserId());
			if (user.isPresent()) {
				model.addAttribute("user", user.get());
			} else {
				model.addAttribute("error", "User không tồn tại!");
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

		return "/User/payment";
	}

	// lấy người dùng với id
	@GetMapping("/api-get-user/{userId}")
	public String getUserById(@PathVariable("userId") Long userId, Model model) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			model.addAttribute("user", user.get());
		} else {
			model.addAttribute("user", new User());
			model.addAttribute("error", "User không tồn tại!");
			System.out.println("không tìm thấy user với ID: " + userId);
		}
		return "/User/payment";
	}

	// lấy tất cả province (tỉnh, thành)
	@GetMapping("/api-get-province")
	@ResponseBody
	// muốn giữ lại @Controller (ở đầu) thì dùng @ResponseBody để chỉ định rằng
	// phương thức này sẽ trả về dữ liệu, không phải một view/template:
//	public String getAllProvince(Model model) {
//	    List<Province> provinces = provinceRepository.findAll();
//	    model.addAttribute("provinces", provinces); // Thêm danh sách tỉnh vào model
//	    return "/User/payment"; // Đổi "your-view" thành tên view của bạn
//	}
	public List<Province> getAllProvince() {
		return provinceRepository.findAll();
	}

	// lấy tất cả district (quận, huyện) theo province
	@ResponseBody
	@GetMapping("/api-get-district/{provinceId}")
	// muốn giữ lại @Controller (ở đầu) thì dùng @ResponseBody để chỉ định rằng
	// phương thức này sẽ trả về dữ liệu, không phải một view/template:
	public List<District> getAllDistrict(@PathVariable int provinceId) {
		return districtRespository.findByProvinceId(provinceId);
	}

	@ResponseBody
	@GetMapping("/api-get-ward/{districtId}")
	// muốn giữ lại @Controller (ở đầu) thì dùng @ResponseBody để chỉ định rằng
	// phương thức này sẽ trả về dữ liệu, không phải một view/template:
	public List<Ward> getAllWard(@PathVariable int districtId) {
		return wardRepository.findByDistrictId(districtId);
	}

	// láy mã giảm giá theo String
	@ResponseBody
	@GetMapping("/api-get-promotion/{code}")
	// ResponseEntity<?>: ? có thể chứa bất kỳ kiểu dữ liệu nào, bao gồm cả
	// Promotions và String
	public ResponseEntity<?> getPromotionById(@PathVariable String code) {
		Optional<Promotions> promotionOpt = promotionsRepository.findByCode(code);

		if (promotionOpt.isPresent()) {
			return ResponseEntity.ok(promotionOpt.get()); // Trả về thông tin mã giảm giá
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy mã giảm giá");
		}

	}

	// lưu tổng tiền (tiền sau khi giảm giá)
	@PutMapping("/api-update-totalprice")
	public ResponseEntity<?> updateTotalPrice(@RequestParam("totalPrice") float totalPrice,
			@RequestParam("bookingId") Long bookingId) {

		try {
			int updatedRows = bookingsRespository.updateTotalPrice(totalPrice, bookingId);
			if (updatedRows > 0) {
				return ResponseEntity.ok("Cập nhật totalPrice thành công cho booking với ID: " + bookingId);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy booking với ID: " + bookingId);
			}
		} catch (Exception e) {
			e.printStackTrace(); // In lỗi ra console để dễ debug
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi cập nhật: " + e.getMessage());
		}
	}

	// tạo payment
	@PostMapping("/api-create-payment")
	public ResponseEntity<Payments> createPayment(@RequestParam("bookingId") Long bookingId,
			@RequestParam("paymentDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date paymentDate,
			@RequestParam("amount") float amount, @RequestParam("paymentMethod") int paymentMethod
			) {
		System.out.println("Amount received from request: " + amount);
		Optional<Bookings> bookingPayment = bookingsRespository.findById(bookingId);
		if (!bookingPayment.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		Bookings existingBooking = bookingPayment.get();

		Payments payment = new Payments();
		payment.setBookingId(existingBooking.getBookingId());
		payment.setPaymentDate(paymentDate);
		payment.setAmount(amount);
		
		payment.setPaymentMethod(paymentMethod);
		payment.setPaymentStatus(2);
		payment.setStatus(true);

		Payments savePayment = paymentsRepository.save(payment);
		System.out.println("Saved Payment amount: " + savePayment.getAmount());

		existingBooking.setPaymentId(savePayment.getPaymentId());
		bookingsRespository.save(existingBooking);
		return ResponseEntity.ok(savePayment);

	}
	
	//dùng để cập nhật paymentStatus sau khi thanh toán thành công
	@PutMapping("/update-status/{bookingId}")
    public ResponseEntity<String> updatePaymentStatus(@PathVariable Long bookingId) {
        paymentsRepository.updatePaymentStatus(bookingId);
        return ResponseEntity.ok("update successfully");
    }
	
	
}
