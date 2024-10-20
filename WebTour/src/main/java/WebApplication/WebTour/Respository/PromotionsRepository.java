package WebApplication.WebTour.Respository;

import java.util.List;
<<<<<<< Updated upstream
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.District;
=======

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
>>>>>>> Stashed changes
import WebApplication.WebTour.Model.Promotions;
import jakarta.transaction.Transactional;

@Repository
public interface PromotionsRepository extends JpaRepository<Promotions, Long>{
<<<<<<< Updated upstream
	@Modifying
	@Transactional
	@Query(value = "SELECT * FROM promotions WHERE code = :code", nativeQuery = true)
	Optional<Promotions> findByCode(@Param("code") String code);
=======

	@Query("SELECT p FROM Promotions p WHERE p.status = true")
	List<Promotions> getPromotionsActive();
	
	@Query("SELECT COUNT(p) > 0 FROM Promotions p WHERE p.code = :code")
	boolean existsByCode(@Param("code") String code);
	
>>>>>>> Stashed changes
}
