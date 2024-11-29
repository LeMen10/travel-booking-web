package WebApplication.WebTour.Controllers.Admin;
import java.sql.Date;
import java.util.ArrayList;
//
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import WebApplication.WebTour.DTO.PaymentHistoryDTO;
import WebApplication.WebTour.Service.StatictisService;

@RestController
public class StatisticsContronller {

	@Autowired
	StatictisService statictisService;
	
	@GetMapping("/api-get-statistics-customer-7-months")
	public ResponseEntity<?> getStatisticCustomer7Months()
	{
		List<Object> statisticsList = statictisService.getSatisticsCustomersLast7Months();
		if(statisticsList == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(statisticsList);
	}
	
	@GetMapping("/api-get-statistics-revenue-7-months")
	public ResponseEntity<?> getStatisticRevenue7Months()
	{
		List<Object> statisticsList = statictisService.getSatisticsRevenueLast7Months();
		if(statisticsList == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(statisticsList);
	}
	
	@GetMapping("/api-get-statistics-new-account-7-months")
	public ResponseEntity<?> getStatisticNewAccount7Months()
	{
		List<Object> statisticsList = statictisService.getStatisticsNewAccountLast7Months();
		if(statisticsList == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(statisticsList);
	}
	
	@GetMapping("/api-get-statistics-booking-7-months")
	public ResponseEntity<?> getStatisticBookings7Months()
	{
		List<Object> statisticsList = statictisService.getStatisticsBookingsLast7Months();
		if(statisticsList == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(statisticsList);
	}
	
	@GetMapping("/api-get-statistics-rate-tour-7-months")
	public ResponseEntity<?> getSatisticsRateAllTourLast7Months()
	{
		List<Object> statisticsList = statictisService.getSatisticsRateAllTourLast7Months();
		if(statisticsList == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(statisticsList);
	}
	
	@GetMapping("/api-get-statistics-original-price-tour-7-months")
	public ResponseEntity<?> getSatisticOriginalPriceAllTourLast7Months()
	{
		List<Object> statisticsList = statictisService.getSatisticsOriginalPriceAllTourLast7Months();
		if(statisticsList == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(statisticsList);
	}
	
	@GetMapping("/api-get-statistics-tour-top")
	public ResponseEntity<?> getSatisticTopTour(@RequestParam("top") int top,
			@RequestParam("total") int total)
	{
		List<Object[]> statisticsList = statictisService.getTopTour(top, total);
		if(statisticsList == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(statisticsList);
	}
	
	@GetMapping("/api-get-statistics-payment")
	public ResponseEntity<?> getSatisticPayment(@RequestParam("status") int status)
	{
		List<Map<Object, Object>> statisticsList = statictisService.listPaymentByStatusAndDay(status);
		
		if(statisticsList == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(statisticsList);
	}
	
	@GetMapping("/api-get-history")
	public ResponseEntity<?> getHistoryPayment(
	        @RequestParam("paymentMethod") int paymentMethod,
	        @RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "bookingId", required = false) Long bookingId,
	        @RequestParam(value = "paymentStatus", required = false) Integer paymentStatus,
	        @RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate,
	        @RequestParam(value = "dateType", required = false) Integer dateType,
	        @RequestParam(value = "amountType", required = false) Integer amountType,
	        @RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum) {
	    
	    Page<PaymentHistoryDTO> listHistory = statictisService.getHistoryPayment(
	            paymentMethod, name, bookingId, paymentStatus, startDate, endDate, dateType, amountType, pageNum);
	    
	    if (listHistory == null || listHistory.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    
	    return ResponseEntity.ok(listHistory);
	}
	
	@GetMapping("/api-get-count-employee")
	public ResponseEntity<?> getCountEmployee()
	{
		int count = statictisService.countAllEmployee();
		return ResponseEntity.ok(count);
	}
	
}





