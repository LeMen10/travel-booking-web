package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.TicketBooking;

@Repository
public interface TicketBookingRepository extends JpaRepository<TicketBooking, Long>{

}
