package WebApplication.WebTour.Controllers.Admin;
//
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
}





