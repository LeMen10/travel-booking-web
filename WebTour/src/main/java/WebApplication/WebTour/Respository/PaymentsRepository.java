package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Payments;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long>{
	
}
