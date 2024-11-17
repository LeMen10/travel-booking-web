package WebApplication.WebTour.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.PromotionProgram;

@Repository
public interface PromotionProgramRepository extends JpaRepository<PromotionProgram, Long>{
	
	@Query("SELECT p FROM PromotionProgram p WHERE p.status = true")
    List<PromotionProgram> getPromotionProgramsActive();
	
}
