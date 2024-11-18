package WebApplication.WebTour.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.TicketBooking;
import WebApplication.WebTour.Respository.TicketBookingRepository;

@Service
public class TicketBookingService {
	@Autowired
	TicketBookingRepository ticketBookingRepository;
	
	//// lấy ticketBooking để hiển thị số lượng người lớn và trẻ em (paymentController)
	public List<TicketBooking> FindTicketBookingById (long bookingId) {
		return ticketBookingRepository.findTicketBookingById(bookingId);
	}
}
