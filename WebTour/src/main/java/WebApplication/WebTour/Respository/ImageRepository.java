package WebApplication.WebTour.Respository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import WebApplication.WebTour.Model.Image;
import WebApplication.WebTour.Model.Tours;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	// sql của hàm List<Image> findByTours với tour là thực thể : select * from image where tour_id=? trang detailTour
	//List<Image> findByTours(Tours tours);
	
	//hiện hình ảnh lên trang detailTour 
	 @Query(value = "SELECT * FROM image WHERE tour_id = :tourId AND status = true", nativeQuery = true)
	 List<Image> getImageOfTour(@Param("tourId") Long tourId);
	
	
	@Transactional
	@Modifying
	@Query("UPDATE Image i SET i.status = false WHERE i.imageId = :imageId")
	void updateStatusByImageId(@Param("imageId") Long imageId);
	
	// Lấy imageId của ảnh nền hiện tại
    @Query("SELECT i.imageId FROM Image i WHERE i.isBackground = true AND i.tours.id = :tourId")
    Long findCurrentBackgroundImageId(@Param("tourId") Long tourId);

    // Cập nhật isBackground = false cho một ảnh
    @Modifying
    @Transactional
    @Query("UPDATE Image i SET i.isBackground = false WHERE i.imageId = :imageId")
    int setBackgroundFalse(@Param("imageId") Long imageId);

    // Cập nhật isBackground = true cho một ảnh
    @Modifying
    @Transactional
    @Query("UPDATE Image i SET i.isBackground = true WHERE i.imageId = :imageId")
    int setBackgroundTrue(@Param("imageId") Long imageId);
    
    @Modifying
    @Transactional
    @Query("UPDATE Image i SET i.status = :status WHERE i.imageId = :imageId")
    void updateStatus(@Param("imageId") Long imageId, @Param("status") boolean status);
}
