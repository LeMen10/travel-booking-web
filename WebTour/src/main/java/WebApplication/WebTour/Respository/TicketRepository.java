package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{

}
