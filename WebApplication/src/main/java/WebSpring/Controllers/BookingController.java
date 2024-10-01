//package WebSpring.Controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import WebSpring.Model.Customer;
//
//import org.springframework.stereotype.Controller;
//
//public class BookingController {
//	
//	
//	@GetMapping("/booking")
//	public ResponseEntity<?> listAllCustomers() {
//		List<bookings> listBooking = bookingRespository.findAll();
//		if (listBooking.isEmpty()) {
//			return new ResponseEntity<>("No booking found", HttpStatus.OK); // Return a message if the list is empty
//		}
//		return new ResponseEntity<>(listBooking, HttpStatus.OK);
//	}
//}
