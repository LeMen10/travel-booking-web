package WebApplication.WebTour.Controllers.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Model.Payments;
import WebApplication.WebTour.Model.Ticket;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.BookingsRespository;
import WebApplication.WebTour.Respository.PaymentsRepository;
import WebApplication.WebTour.Respository.TicketRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class PaymentController {
	@Autowired
	PaymentsRepository paymentsRepository;
	@Autowired
	BookingsRespository bookingsRespository;

	public PaymentController(BookingsRespository bookingsRespository) {
		this.bookingsRespository = bookingsRespository;
	}

	// tạo booking
//	@PostMapping("/create-booking")
//    public ResponseEntity<Bookings> createBooking(
//    		@RequestParam("tourId") int tourId, 
//            @RequestParam("userId") int userId, 
//            @RequestParam("bookingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingDate, 
//            @RequestParam("payStatus") int payStatus, 
//            @RequestParam("peopleNums") int peopleNums) {
//        
//        try {
//        	bookingsRespository.insertBooking(tourId, userId, bookingDate, payStatus, peopleNums);
//            
//            Bookings booking = bookingsRespository.findLastInsertedBooking();
//            
//            if (booking != null) {
//                return ResponseEntity.ok(booking);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//            }
//            
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//		
//    }
	// tạo booking
	@PostMapping("/create-booking") public ResponseEntity<Bookings>
	  createBooking(
	  @RequestParam("tourId") int tourId,
	  @RequestParam("userId") int userId,
	  @RequestParam("bookingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingDate,
	  @RequestParam("peopleNums") int peopleNums) {
		
	  Bookings booking = new Bookings(); booking.setTourId(tourId);
	  booking.setUserId(userId); booking.setBookingDate(bookingDate);
	  booking.setPeopleNums(peopleNums); //JPA đã cung cấp sẵn phương thức "save",
//	  sẽ trả về đối tượng vừa chèn, nên không cần viết hàm inser ở file respository
	  Bookings savedBooking = bookingsRespository.save(booking); return
	  ResponseEntity.ok(savedBooking); 
	  }

	// lấy id booking vừa tạo và mở trang payment
	@GetMapping("/payment/{bookingId}")
	public String openPaymentForm(@PathVariable("bookingId") Long bookingId, Model model) {
		Optional<Payments> payment = paymentsRepository.findById(bookingId);
		// Kiểm tra nếu có giá trị trong Optional
		if (payment.isPresent()) {
			model.addAttribute("payment", payment.get());
		} else {
			model.addAttribute("error", "payment không tồn tại!");
		}
		return "/User/payment";
	}

}
