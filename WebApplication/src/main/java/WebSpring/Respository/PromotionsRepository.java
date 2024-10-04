package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Promotions;

@Repository
public interface PromotionsRepository extends JpaRepository<Promotions, Long>{

}
