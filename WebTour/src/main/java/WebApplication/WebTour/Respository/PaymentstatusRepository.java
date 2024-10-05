package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Paymentstatus;


@Repository
public interface PaymentstatusRepository extends JpaRepository<Paymentstatus, Long>{

}
