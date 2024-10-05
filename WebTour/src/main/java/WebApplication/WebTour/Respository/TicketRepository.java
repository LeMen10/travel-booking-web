package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Ticket;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{

}
