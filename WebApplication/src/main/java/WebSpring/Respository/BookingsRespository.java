package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Bookings;

@Repository
public interface BookingsRespository extends JpaRepository<Bookings, Long>{

}
