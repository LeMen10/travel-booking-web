package WebApplication.WebTour.Respository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.PromotionProgram;

@Repository
public interface PromotionProgramRepository extends JpaRepository<PromotionProgram, Long>{
	
	@Query("SELECT p FROM PromotionProgram p WHERE p.status = true")
	Page<Object[]> getPromotionProgramsActive(Pageable pageable);

	@Query("SELECT p FROM PromotionProgram p WHERE p.startDate >= :startDate AND p.endDate <= :endDate")
	Page<Object[]> findPromotionProgramByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);
}
