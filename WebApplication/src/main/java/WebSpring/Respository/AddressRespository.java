package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Address;

@Repository
public interface AddressRespository extends JpaRepository<Address, Long>{

}
