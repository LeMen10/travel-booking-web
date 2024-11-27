package WebApplication.WebTour.Respository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import WebApplication.WebTour.Model.Payments;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long>{

	@Query(value = "SELECT COUNT(p.payment_id) FROM payments p WHERE p.status = 1 AND p.payment_status = 1 AND p.payment_date = :day", nativeQuery = true)
	List<Map<String, Object>> getWeeklyTotalPayments(@Param("day") Date day);
	
}
