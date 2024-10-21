package WebApplication.WebTour.Respository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import WebApplication.WebTour.Model.Promotions;
import jakarta.transaction.Transactional;

@Repository
public interface PromotionsRepository extends JpaRepository<Promotions, Long>{


	@Modifying
	@Transactional
	@Query(value = "SELECT * FROM promotions WHERE code = :code", nativeQuery = true)
	Optional<Promotions> findByCode(@Param("code") String code);


	@Query("SELECT p FROM Promotions p WHERE p.status = true")
	List<Promotions> getPromotionsActive();
	
	@Query("SELECT COUNT(p) > 0 FROM Promotions p WHERE p.code = :code")
	boolean existsByCode(@Param("code") String code);
	
	@Query("SELECT p FROM Promotions p WHERE p.startDate >= :startDate AND p.endDate <= :endDate")
    List<Promotions> findPromotionsByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
}
