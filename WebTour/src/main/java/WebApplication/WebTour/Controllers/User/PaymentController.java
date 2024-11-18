package WebApplication.WebTour.Controllers.User;

import java.util.Collections;
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
import WebApplication.WebTour.Model.Image;
import WebApplication.WebTour.Model.Paymentmethod;
import WebApplication.WebTour.Model.Payments;
import WebApplication.WebTour.Model.Promotiondetail;
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
import WebApplication.WebTour.Respository.ImageRepository;
import WebApplication.WebTour.Respository.PaymentmethodRespository;
import WebApplication.WebTour.Respository.PaymentsRepository;
import WebApplication.WebTour.Respository.PromotiondetailRepository;
import WebApplication.WebTour.Respository.PromotionsRepository;
import WebApplication.WebTour.Respository.ProvinceRepository;
import WebApplication.WebTour.Respository.TicketBookingRepository;
import WebApplication.WebTour.Respository.TicketRepository;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Respository.UserRepository;
import WebApplication.WebTour.Respository.WardRepository;
import WebApplication.WebTour.Service.BookingService;
import WebApplication.WebTour.Service.TicketBookingService;

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
	@Autowired
	PaymentmethodRespository paymentmethodRespository;
	@Autowired
	PromotiondetailRepository promotiondetailRepository;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	BookingService bookingService;
	@Autowired
	TicketBookingService ticketBookingService;

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
			User user = booking.getUser();
			if (user != null) {
				model.addAttribute("user", user);
				Address address = user.getAddress();
				Province provinceUser = address.getProvince();
				Ward wardUser = address.getWard();
				District districtUser = address.getDistrict();
				
				model.addAttribute("address", address);
				model.addAttribute("provinceUser", provinceUser);
				model.addAttribute("wardUser", wardUser);
				model.addAttribute("districtUser", districtUser);
				List<Province> province = provinceRepository.findAll();
				List<District> district = districtRespository.findAll();
				List<Ward> ward = wardRepository.findAll();
				model.addAttribute("province",province);
				model.addAttribute("district",district);
				model.addAttribute("ward",ward);
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
			
			// lấy thông tin của tour từ clone
			Tours tourPayment = booking.getTour();
			Tours orginalTour = toursRepository.findById(tourPayment.getOriginalId()).get();
			
			if (tourPayment != null) {
				model.addAttribute("tourPayment", tourPayment);
				// Lấy hình ảnh của tour
				List<Image> images = imageRepository.findByTours(orginalTour);
				if (!images.isEmpty()) {

					Image imagePayment = images.get(0); // hình đầu
					model.addAttribute("imagePayment", imagePayment);
				} else {
					model.addAttribute("error", "Không có hình ảnh cho tour này!");
				}
			} else {
				model.addAttribute("error", "TourPayment không tồn tại!");
			}
			// lấy ticketBooking để hiển thị số lượng người lớn và trẻ em
			List<TicketBooking> ticketBooking = ticketBookingService.FindTicketBookingById(booking.getBookingId());
			if (!ticketBooking.isEmpty()) {
				model.addAttribute("ticketBookings", ticketBooking);
				System.out.println(ticketBooking);
			} else {
				model.addAttribute("error", "ticketBooking không tồn tại!");
			}

		} else {
			model.addAttribute("error", "Booking không tồn tại!");
		}

		Bookings bookings = bookingsRespository.findById(bookingId).get();
		User user = bookings.getUser();
		Tours tour = bookings.getTour();
		List<TicketBooking> ticketBookings = ticketBookingRepository.findTicketBookingById(bookingId);
		model.addAttribute("bookingPayment", bookings);
		model.addAttribute("tourPayment", tour);
		model.addAttribute("user", user);
		model.addAttribute("ticketBooking", ticketBookings);

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
			int updatedRows = bookingService.UpdateTotalPrice(totalPrice, bookingId);
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
			@RequestParam("amount") float amount, @RequestParam("paymentMethodId") long paymentMethodId,
			@RequestParam("promotionCode") String promotionCode, 
			@RequestParam("captureId") String captureId,
			@RequestParam("totalUSD") float totalPriceUSD) {
		System.out.println("Amount received from request: " + amount);
		Optional<Bookings> bookingPayment = bookingsRespository.findById(bookingId);
		if (!bookingPayment.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		Bookings existingBooking = bookingPayment.get();

		Payments payment = new Payments();
		payment.setPaymentDate(paymentDate);
		payment.setAmount(amount);

		Paymentmethod paymentMethod = paymentmethodRespository.findById(paymentMethodId).get();
		payment.setPaymentMethod(paymentMethod);
		payment.setPaymentStatus(2);
		payment.setPromotionCode(promotionCode);
		payment.setCaptureId(captureId);
		payment.setTotalPriceDolar(totalPriceUSD);
		payment.setStatus(true);

		Payments savePayment = paymentsRepository.save(payment);
		System.out.println("Saved Payment amount: " + savePayment);

		existingBooking.setPayment(savePayment);
		bookingsRespository.save(existingBooking);
		return ResponseEntity.ok(savePayment);

	}

	// dùng để cập nhật paymentStatus sau khi thanh toán thành công
	@PutMapping("/update-status/{bookingId}")
	public ResponseEntity<String> updatePaymentStatus(@PathVariable Long bookingId) {
		Optional<Bookings> booking = bookingsRespository.findById(bookingId);
		if(booking.isPresent())
		{
			Payments payment = booking.get().getPayment();
			if(payment != null)
			{
				payment.setPaymentStatus(1);
				paymentsRepository.save(payment);
			}
		}
		return ResponseEntity.ok("update successfully");
	}

	// dùng để kiểm tra mã giảm giá đã dùng chưa
	@GetMapping("/api-check-promotion")
	public ResponseEntity<?> checkPromotion(@RequestParam("code") String promotionCode,
			@RequestParam("userId") Long userId, @RequestParam("tourId") int tourId) {

		// kiểm tra mã có dành cho tour hay không
		Optional<Promotiondetail> promotiondetailOpt = promotiondetailRepository
				.getPromotionByTourIdAndPromotionName(tourId, promotionCode);
		if (!promotiondetailOpt.isPresent()) {
			return ResponseEntity.ok(Collections.singletonMap("message", "Mã khuyến mãi không dành cho tour này."));
		}
		// kiểm tra mã đã được sử dụng chưa
		Optional<List<Bookings>> listBooking = bookingsRespository.getUserByBookingId(userId);
		if (listBooking.isPresent()) {

			List<Bookings> bookings = listBooking.get();
			for (Bookings booking : bookings) {
				if (booking.getPayment() == null)
					continue;
				booking.getPayment();
				if (booking.getPayment().getPromotionCode() == null)
					continue;
				if (booking.getPayment().getPromotionCode().equals(promotionCode)) {

					return ResponseEntity.ok(Collections.singletonMap("message", "Mã đã được dùng"));
				}
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Collections.singletonMap("message", "Mã giảm giá hợp lệ."));
	}

	// tạo address trong trang thanh toán
	@PostMapping("/api-create-address")
	public ResponseEntity<?> createAddress(
			@RequestParam Long userId,
			@RequestParam String detail, 
			@RequestParam Long provinceId,
			@RequestParam Long districtId, 
			@RequestParam Long wardId) {
		
		// Tạo address
		Address address = new Address();
		address.setDetail(detail);
		System.out.println(detail);

		// Tìm province, district, ward từ các repository
		Optional<Province> provinceOpt = provinceRepository.findById(provinceId);
		Optional<District> districtOpt = districtRespository.findById(districtId);
		Optional<Ward> wardOpt = wardRepository.findById(wardId);

		if (provinceOpt.isPresent()) {
			address.setProvince(provinceOpt.get());
		}
		if (districtOpt.isPresent()) {
			address.setDistrict(districtOpt.get());
		}
		if (wardOpt.isPresent()) {
			address.setWard(wardOpt.get());
		}
		System.out.println("Detail: " + detail);
		System.out.println("ProvinceId: " + provinceId);
		System.out.println("DistrictId: " + districtId);
		System.out.println("WardId: " + wardId);


		Optional<User> userOpt = userRepository.findById(userId);
		if (userOpt.isPresent()) {
	        User user = userOpt.get();
	        user.setAddress(address);
	        
	        // Lưu vào database
	        addressRespository.save(address);
	        // Lưu địa chỉ mới cho user
	        userRepository.save(user); 

	        return ResponseEntity.ok().body(Collections.singletonMap("message", "Địa chỉ đã được lưu"));
	    } else {
	    	 return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User createAddress không tồn tại!"));
	    }
	}

	@GetMapping("/refund/{bookingid}")
	public String navigateRefundPage(@PathVariable("bookingid") Long bookingid, Model model) {
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
		
		
		model.addAttribute("bookingNotification", booking);
		model.addAttribute("paymentNotification", paymentOpt);
		model.addAttribute("userNotification", userOpt);
		model.addAttribute("tourNotification", tourtOpt);
		model.addAttribute("ticketBookingList", ticketBookingList);
		return "/User/refund";
	}
	
}
