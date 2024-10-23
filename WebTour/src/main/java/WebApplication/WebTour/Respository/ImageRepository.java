package WebApplication.WebTour.Respository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Image;
import WebApplication.WebTour.Model.Tours;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	// sql của hàm List<Image> findByTours với tour là thực thể : select * from image where tour_id=?
	List<Image> findByTours(Tours tours);
}
