package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Paymentstatus;

@Repository
public interface PaymentstatusRepository extends JpaRepository<Paymentstatus, Long>{

}
