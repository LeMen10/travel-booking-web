package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Address;

@Repository
public interface AddressRespository extends JpaRepository<Address, Long>{

}