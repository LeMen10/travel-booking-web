package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Paymentmethod;

@Repository
public interface PaymentmethodRespository extends JpaRepository<Paymentmethod, Long>{

}
