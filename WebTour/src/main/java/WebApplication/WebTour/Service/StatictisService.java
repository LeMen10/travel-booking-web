package WebApplication.WebTour.Service;


import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.DTO.PaymentHistoryDTO;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.AccountRespository;
import WebApplication.WebTour.Respository.BookingsRespository;
import WebApplication.WebTour.Respository.PaymentsRepository;
import WebApplication.WebTour.Respository.ReviewsRepository;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Respository.UserRepository;

@Service
public class StatictisService {
	
	final int GUIDE_ROLE_ID = 2;
	final int ASC =  0;
	final int DESC = 1;
	final int PAGE_SIZE = 5;
	
	@Autowired
	AccountRespository accountRespository;
	
	@Autowired
	BookingsRespository bookingsRespository;
	
	@Autowired
	ToursRepository toursRepository;
	
	@Autowired
	PaymentsRepository paymentsRepository;
	
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
	public List<Object[]> getTopTour(int top, int total)
	{
		return toursRepository.getTotalCustomerTour(total, top);
	}
	
	public List<Map<Object, Object>> listPaymentByStatusAndDay(int status)
	{
		return paymentsRepository.getWeeklyTotalPayments(status);
	}
	
	public Page<PaymentHistoryDTO> getHistoryPayment(int method, String name, Long bookingId, int paymentStatus, 
	        Date startDate, Date endDate, int dateType, int amountType, int pageNum) {
	    
	    Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
	    Page<Object[]> rawData = null;

	    // Chọn query theo dateType và amountType
	    if (dateType == ASC && amountType == ASC) {
	        rawData = paymentsRepository.getHistoryPaymentAA(method, name, bookingId, paymentStatus, startDate, endDate, pageable);
	    } else if (dateType == DESC && amountType == DESC) {
	        rawData = paymentsRepository.getHistoryPaymentDD(method, name, bookingId, paymentStatus, startDate, endDate, pageable);
	    } else if (dateType == ASC && amountType == DESC) {
	        rawData = paymentsRepository.getHistoryPaymentAD(method, name, bookingId, paymentStatus, startDate, endDate, pageable);
	    } else {
	        rawData = paymentsRepository.getHistoryPaymentDA(method, name, bookingId, paymentStatus, startDate, endDate, pageable);
	    }

	    // Chuyển đổi từng Object[] thành PaymentHistoryDTO
	    List<PaymentHistoryDTO> paymentHistoryList = rawData.stream().map(data -> {
	        PaymentHistoryDTO dto = new PaymentHistoryDTO(
	            ((Number) data[0]).longValue(), // bookingId
	            (String) data[1],              // momoId
	            (String) data[2],              // captureId
	            (String) data[3],              // fullName
	            null, // paymentDate
	            new BigDecimal(String.valueOf(data[5])), // totalPriceDolar
	            new BigDecimal(String.valueOf(data[6])), // amount
	            (String) data[7]               // paymentStatus
	        );

	        // Xử lý conversion từ Timestamp sang Date
	        Timestamp timestamp = (Timestamp) data[4];
	        Date sqlDate = new Date(timestamp.getTime());
	        dto.setPaymentDate(sqlDate);
	        return dto;
	    }).collect(Collectors.toList());

	    // Tạo Page<PaymentHistoryDTO> từ PageImpl
	    return new PageImpl<>(paymentHistoryList, pageable, rawData.getTotalElements());
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
