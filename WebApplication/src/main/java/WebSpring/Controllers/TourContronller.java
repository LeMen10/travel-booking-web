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
//import org.springframework.ui.Model;
//
//@Controller // Use @Controller for returning views
//public class TourContronller {
//	
//	
//	@GetMapping("/tour")
//    public ResponseEntity<?> listAllTour() {
//        List<tours> listTour = tourRespository.findAll();
//        if (listTour.isEmpty()) {
//            return new ResponseEntity<>("No tour found", HttpStatus.OK);  // Return a message if the list is empty
//        }
//        return new ResponseEntity<>(listTour, HttpStatus.OK);
//    }
//	
// 
//}
