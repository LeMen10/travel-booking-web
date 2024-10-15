package WebApplication.WebTour.Respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.TicketBooking;
import jakarta.transaction.Transactional;

@Repository
public interface TicketBookingRepository extends JpaRepository<TicketBooking, Long> {
//	@Query(value = "SELECT t.ticket_id, t.name " + "FROM Ticket t "
//			+ "JOIN Ticketbooking tb ON t.ticket_id = tb.ticket_id "
//			+ "JOIN Bookings b ON tb.booking_id = b.booking_id " 
//			+ "JOIN User u ON b.userId = u.user_Id"
//			+ "JOIN Tours tr ON b.tour_id = tr.tour_id "
//			+ "WHERE b.user_id = :userId")
//	List<Object[]> findTicketInfoByUserId(@Param("userId") long id);
	@Modifying
	@Transactional
	@Query("SELECT tb FROM TicketBooking tb WHERE tb.bookingId = :bookingId and tb.status = true")
	List<TicketBooking> findTicketBookingById(@Param("bookingId") long bookingId);
//	@Query("SELECT tb FROM TicketBooking tb JOIN FETCH tb.ticket WHERE tb.bookingId = :bookingId AND tb.status = true")
//	List<TicketBooking> findTicketBookingById(@Param("bookingId") long bookingId);
	
	
}
