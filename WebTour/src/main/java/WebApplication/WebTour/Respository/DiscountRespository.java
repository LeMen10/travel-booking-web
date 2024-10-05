package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Discount;

@Repository
public interface DiscountRespository extends JpaRepository<Discount, Long>{

}
