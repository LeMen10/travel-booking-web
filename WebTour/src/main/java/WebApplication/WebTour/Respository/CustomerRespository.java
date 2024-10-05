package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Customer;
@Repository
public interface CustomerRespository extends JpaRepository<Customer, Long> {
	

}
