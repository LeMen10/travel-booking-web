package WebApplication.WebTour.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import WebApplication.WebTour.Respository.BookingsRespository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
	@Autowired
	BookingsRespository bookingsRespository;
	
	@Transactional(readOnly = true)
    public Page<Object[]> ShowDataTable(Long userId, Pageable pageable) {
        return bookingsRespository.showDataTable(userId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Object[]> FilterOrderPage(Long userId, Integer paymentStatus, Pageable pageable) {
        return bookingsRespository.filterOrderPage(userId, paymentStatus , pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Object[]> SearchDeparture(Long userId, String search, Pageable pageable) {
        return bookingsRespository.searchDeparture(userId, search , pageable);
    }
}
