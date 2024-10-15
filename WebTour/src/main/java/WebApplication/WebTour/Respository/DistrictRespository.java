package WebApplication.WebTour.Respository;

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
public interface DistrictRespository extends JpaRepository<District, Long>{
//	@Modifying
	@Transactional
	@Query(value = "SELECT * FROM district WHERE province_id = :provinceId", nativeQuery = true)
    List<District> findByProvinceId(@Param("provinceId") int provinceId);
}
