package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Paymentmethod;

@Repository
public interface PaymentmethodRespository extends JpaRepository<Paymentmethod, Long>{

}
