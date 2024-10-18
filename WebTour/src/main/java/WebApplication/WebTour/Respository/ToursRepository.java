package WebApplication.WebTour.Respository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Tours;

@Repository
public interface ToursRepository extends JpaRepository<Tours, Long> {

	/*
	 * @Query("SELECT t FROM Tours t WHERE LOWER(t.tourName) LIKE LOWER(CONCAT('%', :tourName, '%'))"
	 * ) List<Tours> findByTourNameContainingIgnoreCase(@Param("tourName") String
	 * tourName);
	 */

//	@Query("SELECT t FROM Tours t WHERE LOWER(t.tourName) LIKE LOWER(CONCAT('%', :tourName, '%'))")
//	Page<Tours> findByTourNameContainingIgnoreCase(@Param("tourName") String tourName, Pageable pageable);
	
	@Query("SELECT t FROM Tours t WHERE "
	        + "(:tourName IS NULL OR LOWER(t.tourName) LIKE LOWER(CONCAT('%', :tourName, '%'))) "
	        + "AND (:startDate IS NULL OR t.startDate = :startDate) "
	        + "AND (:departure IS NULL OR LOWER(t.departure) LIKE LOWER(CONCAT('%', :departure, '%'))) "
	        + "AND (:destination IS NULL OR LOWER(t.destination) LIKE LOWER(CONCAT('%', :destination, '%')))")
	Page<Tours> findTours(@Param("tourName") String tourName,
	                      @Param("startDate") Date startDate,
	                      @Param("departure") String departure,
	                      @Param("destination") String destination,
	                      Pageable pageable);
}
