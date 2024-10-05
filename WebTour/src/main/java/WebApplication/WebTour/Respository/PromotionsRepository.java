package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Promotions;

@Repository
public interface PromotionsRepository extends JpaRepository<Promotions, Long>{

}
