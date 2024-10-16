package WebApplication.WebTour.Controllers.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Model.Payments;
import WebApplication.WebTour.Model.Ticket;
import WebApplication.WebTour.Model.TicketBooking;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.BookingsRespository;
import WebApplication.WebTour.Respository.PaymentsRepository;
import WebApplication.WebTour.Respository.TicketBookingRepository;
import WebApplication.WebTour.Respository.TicketRepository;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Respository.UserRepository;

@Controller // Use @Controller for returning views
public class DetailTourController {
	@Autowired
	ToursRepository toursRepository;
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	BookingsRespository bookingsRespository;
	@Autowired
	PaymentsRepository paymentsRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	TicketBookingRepository ticketBookingRepository;

	public DetailTourController(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	@GetMapping("/detail-tour")
	public String GetDetailTour(Model model) {
		Optional<Tours> detailTour = toursRepository.findById(1l);
		model.addAttribute(detailTour);
		return "/User/detailTour";
	}

	@GetMapping("/detail-tour/{id}") // hiển thị dữ liêu lên trang detail-tour
	public String getDetailTour(@PathVariable("id") Long id, Model model) {
		Optional<Tours> detailTour = toursRepository.findById(id);
		// Kiểm tra nếu có giá trị trong Optional
		if (detailTour.isPresent()) {
			model.addAttribute("tour", detailTour.get());
		} else {
			model.addAttribute("error", "Tour không tồn tại!");
		}
		return "/User/detailTour";
	}

	@GetMapping("/api-get-detail-tour") // để sử dụng cho dấu cộng, trừ số lượng vé (trẻ em hay người lớn)
	public ResponseEntity<Tours> HandelIncrement(@RequestParam("id") Long id) {
		Optional<Tours> detailTour = toursRepository.findById(id);
		// Kiểm tra nếu có giá trị trong Optional
		return detailTour.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

	}

	@GetMapping("/api-get-ticket") // để sử dụng lấy loại vé (trẻ em hay người lớn)
	public ResponseEntity<List<Ticket>> HandelGetTicket() {
		try {
			List<Ticket> tickets = ticketRepository.findAll();

			if (tickets.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			} else {
				return ResponseEntity.ok(tickets);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// tạo booking
	@PostMapping("/create-booking")
	public ResponseEntity<Bookings> createBooking(@RequestParam("tourId") int tourId,
			@RequestParam("userId") int userId,
			@RequestParam("bookingDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingDate,
			@RequestParam("quantityAdult") int quantityAdult, 
			@RequestParam("quantityChild") int quantityChild) {

		Bookings booking = new Bookings();
		booking.setTourId(tourId);
		booking.setUserId(userId);
		booking.setBookingDate(bookingDate);
		
		booking.setPeopleNums(quantityAdult+quantityChild); 
		
		// JPA đã cung cấp sẵn phương thức "save",sẽ trả về đối tượng vừa chèn, nên không cần viết hàm insert ở file respository
		Bookings savedBooking = bookingsRespository.save(booking);
		handleQuantityTicket(Integer.parseInt(savedBooking.getBookingId().toString())  , quantityAdult, quantityChild);
		return ResponseEntity.ok(savedBooking);
	}

	public void handleQuantityTicket(int bookingId,int quantityAdult, int quantityChild) {
		 if(quantityAdult > 0) {
			TicketBooking  ticketAdult = new TicketBooking();
			Ticket ticket = ticketRepository.findById(2l).get();
			ticketAdult.setTicket(ticket);
			ticketAdult.setBookingId(bookingId);
			ticketAdult.setQuantity(quantityAdult);
			TicketBooking saveticketBooking = ticketBookingRepository.save(ticketAdult);
		}
		if (quantityChild > 0){
			TicketBooking  ticketChild = new TicketBooking();
			Ticket ticket = ticketRepository.findById(1l).get();
			ticketChild.setTicket(ticket);
			ticketChild.setBookingId(bookingId);
			ticketChild.setQuantity(quantityChild);
			TicketBooking saveticketBooking = ticketBookingRepository.save(ticketChild);
		
		}
	}

//	@GetMapping("/api-get-type-ticket")
//	public ResponseEntity<List<Ticket>> getTypeTicket(@RequestParam("userId") long userId) {
//	    List<Object[]> ticket = ticketBookingRepository.findTicketInfoByUserId(userId);
//	    
//	    // đổi từ Object[] sang Ticket
//	    List<Ticket> ticketList = ticket.stream()
//	            .map(obj -> new Ticket())
//	            .collect(Collectors.toList());
//	    
//	    if (!ticketList.isEmpty()) {
//	        return ResponseEntity.ok(ticketList);
//	    } else {
//	        return ResponseEntity.notFound().build();
//	    }
//	}

}
