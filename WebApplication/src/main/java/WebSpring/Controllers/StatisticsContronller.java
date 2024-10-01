//package WebSpring.Controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import WebSpring.Model.Customer;
//import WebSpring.Respository.CustomerRespository;
//
//public class StatisticsContronller {
//
//	@Autowired
//	StatisticsRespository statisticsRespository;
//	
//	@GetMapping("/review")
//    public ResponseEntity<?> listAllCustomers() {
//        List<review> listReview = reviewRespository.findAll();
//        if (listReview.isEmpty()) {
//            return new ResponseEntity<>("No review found", HttpStatus.OK);  // Return a message if the list is empty
//        }
//        return new ResponseEntity<>(listReview, HttpStatus.OK);
//    }
//}
