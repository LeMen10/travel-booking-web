package WebApplication.WebTour.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.BookingsRespository;
import WebApplication.WebTour.Respository.PaymentsRepository;
import WebApplication.WebTour.Respository.UserRepository;

@Service
public class BookingService {

	@Autowired
	BookingsRespository bookingsRespository;
	
	@Autowired
	PaymentsRepository paymentsRepository;

	@Autowired
	UserRepository userRepository;
	
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
	
	public boolean addUserToBooking(long userId,long bookingId)
	{
		Optional<Bookings> booking = bookingsRespository.findById(bookingId);
		Optional<User> user = userRepository.findById(userId);
        if (booking != null) {
        	Bookings bookingEntity = booking.get();
        	bookingEntity.setUser(user.get());
        	return bookingsRespository.save(bookingEntity) != null;
        }
        return false;
	}
	
	public int UpdateTotalPrice (float totalPrice, Long bookingId) {
		return bookingsRespository.updateTotalPrice(totalPrice, bookingId);
	}
}
