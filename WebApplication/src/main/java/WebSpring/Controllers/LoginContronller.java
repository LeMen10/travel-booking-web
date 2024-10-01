//package WebSpring.Controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import WebSpring.Model.Customer;
//import WebSpring.Respository.CustomerRespository;
//
//public class LoginContronller {
//
//	@Autowired
//	CustomerRespository customerRespository;
//	TourRespository tourRespository;
//	AccountRespository accountRespository;
//
//	@GetMapping("/customer")
//	public ResponseEntity<?> listAllCustomers() {
//		List<Customer> listCustomer = customerRespository.findAll();
//		if (listCustomer.isEmpty()) {
//			return new ResponseEntity<>("No customers found", HttpStatus.OK); // Return a message if the list is empty
//		}
//		return new ResponseEntity<>(listCustomer, HttpStatus.OK);
//	}
//
//	@GetMapping("/employee")
//	public ResponseEntity<?> listAllEmployee() {
//		List<employees> listEmployee = employeeRespository.findAll();
//		if (listEmployee.isEmpty()) {
//			return new ResponseEntity<>("No employee found", HttpStatus.OK); // Return a message if the list is empty
//		}
//		return new ResponseEntity<>(listEmployee, HttpStatus.OK);
//	}
//	
//	@GetMapping("/account")
//	public ResponseEntity<?> listAllAccount() {
//		List<account> listAccount = accountRespository.findAll();
//		if (listAccount.isEmpty()) {
//			return new ResponseEntity<>("No account found", HttpStatus.OK); // Return a message if the list is empty
//		}
//		return new ResponseEntity<>(listAccount, HttpStatus.OK);
//	}
//}
