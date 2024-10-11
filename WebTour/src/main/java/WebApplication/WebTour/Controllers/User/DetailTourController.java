package WebApplication.WebTour.Controllers.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.Ticket;
import WebApplication.WebTour.Model.TicketBooking;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.TicketBookingRepository;
import WebApplication.WebTour.Respository.TicketRepository;
import WebApplication.WebTour.Respository.ToursRepository;

@Controller // Use @Controller for returning views
public class DetailTourController {
	@Autowired
	ToursRepository toursRepository;
	@Autowired
	TicketRepository ticketRepository;

	public DetailTourController(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}

	@GetMapping("/detail-tour") // hiển thị dữ liêu lên trang detail-tour
	public String GetDetailTour(Model model) {
		Optional<Tours> detailTour = toursRepository.findById(1l);
		model.addAttribute(detailTour);
		return "/User/detailTour";
	}

	@GetMapping("/detail-tour/{id}")
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
