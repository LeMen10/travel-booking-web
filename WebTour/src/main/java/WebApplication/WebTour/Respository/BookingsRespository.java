package WebApplication.WebTour.Respository;

import java.util.List;
import java.util.Optional;

//import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Model.Payments;

import org.springframework.transaction.annotation.Transactional;

@Repository

public interface BookingsRespository extends JpaRepository<Bookings, Long> {

// cập nhật giá khi 
	@Modifying
	@Transactional
	@Query(value = "UPDATE bookings SET total_price = :totalPrice WHERE booking_id = :bookingId AND status = true", nativeQuery = true)
	int updateTotalPrice(@Param("totalPrice") float totalPrice, @Param("bookingId") Long bookingId);

	/*
	 * //hiện danh sách lên bảng trên trang order(đơn hàng của bạn)
	 * 
	 * @Query(value =
	 * "SELECT b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	 * + "SUM(tb.quantity) AS totalQuantity, " +
	 * "GROUP_CONCAT(CONCAT(t.name, ': ', tb.quantity) SEPARATOR ', ') AS ticketDetails, "
	 * + "b.total_price, tours.start_date, tours.departure, p.payment_id " +
	 * "FROM bookings b " + "LEFT JOIN payments p ON p.booking_id = b.booking_id " +
	 * "LEFT JOIN ticketbooking tb ON tb.booking_id = b.booking_id " +
	 * "LEFT JOIN ticket t ON t.ticket_id = tb.ticket_id " +
	 * "JOIN tours tours ON tours.tour_id = b.tour_id " +
	 * "WHERE b.user_id = :userId AND b.status = true " +
	 * "GROUP BY b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	 * + "b.total_price, tours.start_date, tours.departure, p.payment_id " +
	 * "ORDER BY b.booking_date DESC", nativeQuery = true) List<Object[]>
	 * showDataTable(@Param("userId") Long userId);
	 */
	//hiện lên table trang đơn hàng của bạn
	@Transactional
	@Query(value = "SELECT b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
			+ "SUM(tb.quantity) AS totalQuantity, "
			+ "GROUP_CONCAT(CONCAT(t.name, ': ', tb.quantity) SEPARATOR ', ') AS ticketDetails, "
			+ "b.total_price, tours.start_date, tours.departure, p.payment_id " 
			+ "FROM bookings b "
			+ "LEFT JOIN payments p ON p.payment_id = b.payment_id "
			+ "LEFT JOIN ticketbooking tb ON tb.booking_id = b.booking_id "
			+ "LEFT JOIN ticket t ON t.ticket_id = tb.ticket_id " + "JOIN tours tours ON tours.tour_id = b.tour_id "
			+ "WHERE b.user_id = :userId AND b.status = true "
			+ "GROUP BY b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
			+ "b.total_price, tours.start_date, tours.departure, p.payment_id "
			+ "ORDER BY b.booking_date DESC", countQuery = "SELECT COUNT(*) FROM bookings b WHERE b.user_id = :userId AND b.status = true", nativeQuery = true)
	Page<Object[]> showDataTable(@Param("userId") Long userId, Pageable pageable);
	
	/*
	 * @Transactional
	 * @Modifying
	 * @Query(value =
	 * "SELECT b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	 * + "SUM(tb.quantity) AS totalQuantity, " +
	 * "GROUP_CONCAT(CONCAT(t.name, ': ', tb.quantity) SEPARATOR ', ') AS ticketDetails, "
	 * + "b.total_price, tours.start_date, tours.departure, p.payment_id " +
	 * "FROM bookings b " + "LEFT JOIN payments p ON p.booking_id = b.booking_id " +
	 * "JOIN ticketbooking tb ON tb.booking_id = b.booking_id " +
	 * "JOIN ticket t ON t.ticket_id = tb.ticket_id " +
	 * "JOIN tours tours ON tours.tour_id = b.tour_id " +
	 * "WHERE b.user_id = :userId AND b.status = true " +
	 * "AND (:paymentStatus IS NULL " // lấy hết +
	 * "OR (:paymentStatus = 1 AND p.payment_id IS NOT NULL) " +
	 * "OR (:paymentStatus = 2 AND p.payment_id IS NULL)) " +
	 * "GROUP BY b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	 * + "b.total_price, tours.start_date, tours.departure, p.payment_id " +
	 * "ORDER BY b.booking_date DESC", nativeQuery = true) List<Object[]>
	 * filterOrderPage(@Param("userId") Long userId, @Param("paymentStatus") Integer
	 * paymentStatus);
	 */
	// lọc theo paid hoặc unpaid
	@Transactional
	@Query(value = "SELECT b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
			+ "SUM(tb.quantity) AS totalQuantity, "
			+ "GROUP_CONCAT(CONCAT(t.name, ': ', tb.quantity) SEPARATOR ', ') AS ticketDetails, "
			+ "b.total_price, tours.start_date, tours.departure, p.payment_id " + "FROM bookings b "
			+ "LEFT JOIN payments p ON p.payment_id = b.payment_id "
			+ "JOIN ticketbooking tb ON tb.booking_id = b.booking_id " + "JOIN ticket t ON t.ticket_id = tb.ticket_id "
			+ "JOIN tours tours ON tours.tour_id = b.tour_id " + "WHERE b.user_id = :userId AND b.status = true "
			+ "AND (:paymentStatus IS NULL " // lấy hết
			+ "OR (:paymentStatus = 1 AND p.payment_id IS NOT NULL) "
			+ "OR (:paymentStatus = 2 AND p.payment_id IS NULL)) "
			+ "GROUP BY b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
			+ "b.total_price, tours.start_date, tours.departure, p.payment_id "
			+ "ORDER BY b.booking_date DESC", nativeQuery = true)
	Page<Object[]> filterOrderPage(@Param("userId") Long userId, 
			@Param("paymentStatus") Integer paymentStatus, Pageable pageable);
	

	@Query(value = "SELECT COALESCE(SUM(b.people_nums), 0), m.month, YEAR(CURDATE()) AS year "
			+ "FROM (SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION "
			+ " SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION "
			+ " SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) AS m "
			+ "LEFT JOIN Bookings b ON m.month = MONTH(b.booking_date) AND YEAR(b.booking_date) = YEAR(CURDATE()) AND b.status = 1 "
			+ "WHERE m.month <= MONTH(CURDATE()) "
			+ "GROUP BY  m.month ORDER BY  m.month DESC LIMIT 7", nativeQuery = true)
	Optional<List<Object>> getSatisticsCustomersLast7Months();
	
	@Query(value = "SELECT COALESCE(SUM(b.total_price), 0), m.month,  YEAR(CURDATE()) AS year "
			+ "FROM (SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION "
			+ " SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION "
			+ " SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) AS m "
			+ "LEFT JOIN Bookings b ON m.month = MONTH(b.booking_date) AND YEAR(b.booking_date) = YEAR(CURDATE()) AND b.status = 1 "
			+ "WHERE m.month <= MONTH(CURDATE()) "
			+ "GROUP BY  m.month ORDER BY  m.month DESC LIMIT 7", nativeQuery = true)
	Optional<List<Object>> getSatisticsRevenueLast7Months();
	
	@Query(value = "SELECT COALESCE(COUNT(b.booking_id), 0), m.month,  YEAR(CURDATE()) AS year "
			+ "FROM (SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION "
			+ " SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION "
			+ " SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) AS m "
			+ "LEFT JOIN Bookings b ON m.month = MONTH(b.booking_date) AND YEAR(b.booking_date) = YEAR(CURDATE()) AND b.status = 1 "
			+ "LEFT JOIN Payments p ON b.payment_id = p.payment_id AND p.status = 1 AND p.payment_status = 1 "
			+ "WHERE m.month <= MONTH(CURDATE()) "
			+ "GROUP BY  m.month ORDER BY  m.month DESC LIMIT 7", nativeQuery = true)
	Optional<List<Object>> getSatisticsBookingsLast7Months();

	
	// tìm kiếm theo khởi hành
	@Transactional
	@Query(value = "SELECT b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
			+ "SUM(tb.quantity) AS totalQuantity, "
			+ "GROUP_CONCAT(CONCAT(t.name, ': ', tb.quantity) SEPARATOR ', ') AS ticketDetails, "
			+ "b.total_price, tours.start_date, tours.departure, p.payment_id " + "FROM bookings b "
			+ "LEFT JOIN payments p ON p.payment_id = b.payment_id "
			+ "LEFT JOIN ticketbooking tb ON tb.booking_id = b.booking_id "
			+ "LEFT JOIN ticket t ON t.ticket_id = tb.ticket_id " + "JOIN tours tours ON tours.tour_id = b.tour_id "
			+ "WHERE b.user_id = :userId AND b.status = true "
			+ "AND tours.departure LIKE CONCAT('%', :searchTerm, '%') "
			+ "GROUP BY b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
			+ "b.total_price, tours.start_date, tours.departure, p.payment_id "
			+ "ORDER BY b.booking_date DESC", nativeQuery = true)
	Page<Object[]> searchDeparture(@Param("userId") Long userId, 
			@Param("searchTerm") String search, Pageable pageable);

	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query(value =
	 * "SELECT b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	 * + "SUM(tb.quantity) AS totalQuantity, " +
	 * "GROUP_CONCAT(CONCAT(t.name, ': ', tb.quantity) SEPARATOR ', ') AS ticketDetails, "
	 * + "b.total_price, tours.start_date, tours.departure, p.payment_id " +
	 * "FROM bookings b " + "LEFT JOIN payments p ON p.booking_id = b.booking_id " +
	 * "LEFT JOIN ticketbooking tb ON tb.booking_id = b.booking_id " +
	 * "LEFT JOIN ticket t ON t.ticket_id = tb.ticket_id " +
	 * "JOIN tours tours ON tours.tour_id = b.tour_id " +
	 * "WHERE b.user_id = :userId AND b.status = true " +
	 * "AND tours.departure LIKE CONCAT('%', :searchTerm, '%') " +
	 * "GROUP BY b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	 * + "b.total_price, tours.start_date, tours.departure, p.payment_id " +
	 * "ORDER BY b.booking_date DESC", nativeQuery = true) List<Object[]>
	 * searchDeparture(@Param("userId") Long userId, @Param("searchTerm") String
	 * search);
	 */
}
