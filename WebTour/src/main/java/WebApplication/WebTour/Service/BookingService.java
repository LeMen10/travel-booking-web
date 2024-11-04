package WebApplication.WebTour.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Respository.BookingsRespository;
import WebApplication.WebTour.Respository.PaymentsRepository;

@Service
public class BookingService {

	@Autowired
	BookingsRespository bookingsRespository;
	
	@Autowired
	PaymentsRepository paymentsRepository;
	
	public Optional<Bookings> findById(long bookingId)
	{
		return bookingsRespository.findById(bookingId);
	}
	
	public boolean updateStatusPayment(long bookingId)
	{
		Optional<Bookings> booking = bookingsRespository.findById(bookingId);
        if (booking != null) {
        	Bookings bookingEntity = booking.get();
        	bookingEntity.getPayment().setPaymentStatus(3);
        	return paymentsRepository.save(bookingEntity.getPayment()) != null;
        }
        return false;
	}
}
