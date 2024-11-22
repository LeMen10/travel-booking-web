package WebApplication.WebTour.Respository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Image;
import WebApplication.WebTour.Model.Tours;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	// sql của hàm List<Image> findByTours với tour là thực thể : select * from image where tour_id=? trang detailTour
	//List<Image> findByTours(Tours tours);
	
	//hiện hình ảnh lên trang detailTour 
	 @Query(value = "SELECT * FROM image WHERE tour_id = :tourId AND status = true", nativeQuery = true)
	 List<Image> getImageOfTour(@Param("tourId") Long tourId);
}
