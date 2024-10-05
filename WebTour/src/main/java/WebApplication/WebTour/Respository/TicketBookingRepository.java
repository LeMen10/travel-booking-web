package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.TicketBooking;


@Repository
public interface TicketBookingRepository extends JpaRepository<TicketBooking, Long>{

}
