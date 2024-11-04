package WebApplication.WebTour.Controllers.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Service.BookingService;

@RestController
public class BookingController {
	
	@Autowired
	BookingService bookingService;
	
    @GetMapping("/get-booking/{bookingId}")
    public ResponseEntity<?> getBookingById(@PathVariable long bookingId) {
    
    	Optional<Bookings> booking = bookingService.findById(bookingId);
        
        if (booking != null) {
            return ResponseEntity.ok(booking.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/api-refund-booking/{bookingId}")
    public ResponseEntity<?> updateStatusBooking(@PathVariable long bookingId) {
        if (bookingService.updateStatusPayment(bookingId)) {
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.notFound().build();
    }
	
}



