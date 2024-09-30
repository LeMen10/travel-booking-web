package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Customer;

@Repository
public interface CustomerRespository extends JpaRepository<Customer, Long> {
	

}
