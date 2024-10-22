package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import WebApplication.WebTour.Model.Payments;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long>{
	//dùng để cập nhật paymentStatus sau khi thanh toán thành công
	@Transactional
    @Modifying
    @Query("UPDATE Payments p SET p.paymentStatus = 1 WHERE p.bookingId = :bookingId ")
    int updatePaymentStatus( Long bookingId);
	
	
}
