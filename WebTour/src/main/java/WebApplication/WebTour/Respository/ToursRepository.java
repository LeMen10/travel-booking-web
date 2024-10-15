package WebApplication.WebTour.Respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Tours;

@Repository
public interface ToursRepository extends JpaRepository<Tours, Long> {

	/*
	 * @Query("SELECT t FROM Tours t WHERE LOWER(t.tourName) LIKE LOWER(CONCAT('%', :tourName, '%'))"
	 * ) List<Tours> findByTourNameContainingIgnoreCase(@Param("tourName") String
	 * tourName);
	 */

	@Query("SELECT t FROM Tours t WHERE LOWER(t.tourName) LIKE LOWER(CONCAT('%', :tourName, '%'))")
	Page<Tours> findByTourNameContainingIgnoreCase(@Param("tourName") String tourName, Pageable pageable);

}
