package WebApplication.WebTour.Service;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Respository.AccountRespository;

@Service
public class StatictisService {
	
	@Autowired
	AccountRespository accountRespository;
	
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
