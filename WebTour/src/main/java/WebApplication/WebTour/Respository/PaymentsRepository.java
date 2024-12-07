package WebApplication.WebTour.Respository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import WebApplication.WebTour.DTO.PaymentHistoryDTO;
import WebApplication.WebTour.Model.Payments;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long>{

	@Query(value = "WITH WeekDays AS ("
			+ "    SELECT CURDATE() - INTERVAL (DAYOFWEEK(CURDATE()) - 2) DAY AS week_day "
			+ "    UNION ALL"
			+ "    SELECT CURDATE() - INTERVAL (DAYOFWEEK(CURDATE()) - 3) DAY UNION ALL"
			+ "    SELECT CURDATE() - INTERVAL (DAYOFWEEK(CURDATE()) - 4) DAY UNION ALL"
			+ "    SELECT CURDATE() - INTERVAL (DAYOFWEEK(CURDATE()) - 5) DAY"
			+ "    UNION ALL"
			+ "    SELECT CURDATE() - INTERVAL (DAYOFWEEK(CURDATE()) - 6) DAY"
			+ "    UNION ALL"
			+ "    SELECT CURDATE() - INTERVAL (DAYOFWEEK(CURDATE()) - 7) DAY"
			+ "    UNION ALL"
			+ "    SELECT CURDATE() - INTERVAL (DAYOFWEEK(CURDATE()) - 8) DAY) "
			+ "SELECT  w.week_day AS payment_date, COUNT(p.payment_id) AS payment_count"
			+ "FROM WeekDays w LEFT JOIN payments p "
			+ "ON DATE(p.payment_date) = w.week_day"
			+ "    AND p.status = 1 "
			+ "    AND p.payment_status = 1"
			+ "GROUP BY w.week_day"
			+ "ORDER BY w.week_day;", nativeQuery = true)
	List<Map<Object,Object>> getWeeklyTotalPayments(@Param("status") int status);
	
	@Query(value = "SELECT b.bookingId AS bookingId, p.momoId AS momoId, p.captureId AS captureId, " +
            "b.user.fullName AS fullName, p.paymentDate AS paymentDate, p.totalPriceDolar AS totalPriceDolar, " +
            "p.amount AS amount, pt.name AS paymentStatus" +
            " FROM Payments p " +
            " JOIN Bookings b ON b.payment.paymentId = p.paymentId " +
            " JOIN Paymentstatus pt ON p.paymentStatus = pt.paymentStatusId " +
            " WHERE p.paymentMethod.paymethodId = :method" +
            " AND (:name IS NULL OR LOWER(b.user.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            " AND (:bookingId IS NULL OR b.bookingId = :bookingId) " +
            " AND (:paymentStatus = 0 OR p.paymentStatus = :paymentStatus) " +
            " AND (:startDate IS NULL OR p.paymentDate >= :startDate) " +
            " AND (:endDate IS NULL OR p.paymentDate <= :endDate) " +
            " ORDER BY p.paymentDate ASC, p.amount ASC, p.totalPriceDolar ASC")
	Page<Object[]> getHistoryPaymentAA(@Param("method") int method, @Param("name") String name,
			@Param("bookingId") Long bookingId, @Param("paymentStatus") int paymentStatus,
			@Param("startDate") Date startDate,@Param("endDate") Date endDate, Pageable pageable);
	
	@Query(value = "SELECT b.bookingId AS bookingId, p.momoId AS momoId, p.captureId AS captureId, " +
            "b.user.fullName AS fullName, p.paymentDate AS paymentDate, p.totalPriceDolar AS totalPriceDolar, " +
            "p.amount AS amount, pt.name AS paymentStatus" +
            " FROM Payments p " +
            " JOIN Bookings b ON b.payment.paymentId = p.paymentId " +
            " JOIN Paymentstatus pt ON p.paymentStatus = pt.paymentStatusId " +
            " WHERE p.paymentMethod.paymethodId = :method" +
            " AND (:name IS NULL OR LOWER(b.user.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            " AND (:bookingId IS NULL OR b.bookingId = :bookingId) " +
            " AND (:paymentStatus = 0 OR p.paymentStatus = :paymentStatus) " +
            " AND (:startDate IS NULL OR p.paymentDate >= :startDate) " +
            " AND (:endDate IS NULL OR p.paymentDate <= :endDate) " +
            " ORDER BY p.paymentDate DESC, p.amount DESC, p.totalPriceDolar DESC")
	Page<Object[]> getHistoryPaymentDD(@Param("method") int method, @Param("name") String name,
			@Param("bookingId") Long bookingId, @Param("paymentStatus") int paymentStatus,
			@Param("startDate") Date startDate,@Param("endDate") Date endDate, Pageable pageable);
	
	@Query(value = "SELECT b.bookingId AS bookingId, p.momoId AS momoId, p.captureId AS captureId, " +
            "b.user.fullName AS fullName, p.paymentDate AS paymentDate, p.totalPriceDolar AS totalPriceDolar, " +
            "p.amount AS amount, pt.name AS paymentStatus" +
            " FROM Payments p " +
            " JOIN Bookings b ON b.payment.paymentId = p.paymentId " +
            " JOIN Paymentstatus pt ON p.paymentStatus = pt.paymentStatusId " +
            " WHERE p.paymentMethod.paymethodId = :method" +
            " AND (:name IS NULL OR LOWER(b.user.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            " AND (:bookingId IS NULL OR b.bookingId = :bookingId) " +
            " AND (:paymentStatus = 0 OR p.paymentStatus = :paymentStatus) " +
            " AND (:startDate IS NULL OR p.paymentDate >= :startDate) " +
            " AND (:endDate IS NULL OR p.paymentDate <= :endDate) " +
            " ORDER BY p.paymentDate ASC, p.amount DESC, p.totalPriceDolar DESC")
	Page<Object[]> getHistoryPaymentAD(@Param("method") int method, @Param("name") String name,
			@Param("bookingId") Long bookingId, @Param("paymentStatus") int paymentStatus,
			@Param("startDate") Date startDate,@Param("endDate") Date endDate, Pageable pageable);

	@Query(value = "SELECT b.bookingId AS bookingId, p.momoId AS momoId, p.captureId AS captureId, " +
            "b.user.fullName AS fullName, p.paymentDate AS paymentDate, p.totalPriceDolar AS totalPriceDolar, " +
            "p.amount AS amount, pt.name AS paymentStatus" +
            " FROM Payments p " +
            " JOIN Bookings b ON b.payment.paymentId = p.paymentId " +
            " JOIN Paymentstatus pt ON p.paymentStatus = pt.paymentStatusId " +
            " WHERE p.paymentMethod.paymethodId = :method" +
            " AND (:name IS NULL OR LOWER(b.user.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            " AND (:bookingId IS NULL OR b.bookingId = :bookingId) " +
            " AND (:paymentStatus = 0 OR p.paymentStatus = :paymentStatus) " +
            " AND (:startDate IS NULL OR p.paymentDate >= :startDate) " +
            " AND (:endDate IS NULL OR p.paymentDate <= :endDate) " +
            " ORDER BY p.paymentDate DESC, p.amount ASC, p.totalPriceDolar ASC")
	Page<Object[]> getHistoryPaymentDA(@Param("method") int method, @Param("name") String name,
			@Param("bookingId") Long bookingId, @Param("paymentStatus") int paymentStatus,
			@Param("startDate") Date startDate,@Param("endDate") Date endDate, Pageable pageable);
}
