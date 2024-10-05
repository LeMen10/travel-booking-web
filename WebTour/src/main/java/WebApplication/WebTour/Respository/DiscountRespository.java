package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Discount;

@Repository
public interface DiscountRespository extends JpaRepository<Discount, Long>{

}
