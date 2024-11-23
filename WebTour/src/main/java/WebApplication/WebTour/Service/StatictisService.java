package WebApplication.WebTour.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.AccountRespository;
import WebApplication.WebTour.Respository.BookingsRespository;
import WebApplication.WebTour.Respository.ReviewsRepository;
import WebApplication.WebTour.Respository.UserRepository;

@Service
public class StatictisService {
	
	final int GUIDE_ROLE_ID = 2;
	
	@Autowired
	AccountRespository accountRespository;
	
	@Autowired
	BookingsRespository bookingsRespository;
	
	@Autowired
	ReviewsRepository reviewsRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public List<Object> getSatisticsCustomersLast7Months()
	{
		Optional<List<Object>> statisticsList = bookingsRespository.getSatisticsCustomersLast7Months();
		return statisticsList.isPresent() ? statisticsList.get() :  null;
	}
	
	public List<Object> getSatisticsRevenueLast7Months()
	{
		Optional<List<Object>> statisticsList = bookingsRespository.getSatisticsRevenueLast7Months();
		return statisticsList.isPresent() ? statisticsList.get() :  null;
	}
	
	public List<Object> getStatisticsNewAccountLast7Months()
	{
		Optional<List<Object>> statisticsList = accountRespository.getStatisticsNewAccountLast7Months();
		return statisticsList.isPresent() ? statisticsList.get() :  null;
	}
	
	public List<Object> getStatisticsBookingsLast7Months()
	{
		Optional<List<Object>> statisticsList = bookingsRespository.getSatisticsBookingsLast7Months();
		return statisticsList.isPresent() ? statisticsList.get() :  null;
	}
	
	public List<Object> getSatisticsRateAllTourLast7Months()
	{
		Optional<List<Object>> statisticsList = reviewsRepository.getSatisticsRateAllTourLast7Months();
		return statisticsList.isPresent() ? statisticsList.get() :  null;
	}
	
	public List<Object> getSatisticsOriginalPriceAllTourLast7Months()
	{
		Optional<List<Object>> statisticsList = bookingsRespository.getSatisticsProfitLast7Months();
		return statisticsList.isPresent() ? statisticsList.get() :  null;
	}
	
	public int countAllEmployee()
	{
		List<User> countEmployee = userRepository.findAllEmployee();
		return countEmployee != null? countEmployee.size():  0;
	}

	
	public int statisticsUserByCreaton(Date startDate, Date finishDate)
	{
		if(finishDate == null)
		{
			LocalDate localDate = LocalDate.now();
	        finishDate = Date.valueOf(localDate);
		}
		
//		Integer quantityMember = accountRespository.countUser(startDate, finishDate);
//		
//		if(quantityMember == null) quantityMember = 0;
//		return quantityMember;
		return 0;
	}
	
	
	public int statisticsTourByBooking(Date startDate, Date finishDate)
	{
		if(finishDate == null)
		{
			LocalDate localDate = LocalDate.now();
	        finishDate = Date.valueOf(localDate);
		}
		
		return 0;
	}
	
	public int revenueStatisticsByTime(Date startDate, Date finishDate)
	{
		if(finishDate == null)
		{
			LocalDate localDate = LocalDate.now();
	        finishDate = Date.valueOf(localDate);
		}
		
		return 0;
	}
	
	public int statisticsCustomerByTime(Date startDate, Date finishDate)
	{
		if(finishDate == null)
		{
			LocalDate localDate = LocalDate.now();
	        finishDate = Date.valueOf(localDate);
		}
		
		return 0;
	}
	
	public int statisticsProfitByTime(Date startDate, Date finishDate)
	{
		if(finishDate == null)
		{
			LocalDate localDate = LocalDate.now();
	        finishDate = Date.valueOf(localDate);
		}
		
		return 0;
	}
}
