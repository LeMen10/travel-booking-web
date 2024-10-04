package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Payments;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long>{
	
}
