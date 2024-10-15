package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.District;
import WebApplication.WebTour.Model.Ward;
import jakarta.transaction.Transactional;


@Repository
public interface WardRepository extends JpaRepository<Ward, Long>{
//	@Modifying
	@Transactional
	@Query(value = "SELECT * FROM ward WHERE district_id = :districtId", nativeQuery = true)
    List<Ward> findByDistrictId(@Param("districtId") int districtId);
}
