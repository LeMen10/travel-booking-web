package WebApplication.WebTour.Respository;


import java.util.List;
import java.util.Optional;

//import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Model.Payments;

import org.springframework.transaction.annotation.Transactional;

@Repository

public interface BookingsRespository extends JpaRepository<Bookings, Long> {
//	@Modifying
//    @Transactional
//    @Query(value = "INSERT INTO Bookings (tour_id, user_id, booking_date, pay_status, people_nums) " +
//                   "VALUES (:tourId, :userId, :bookingDate, :payStatus, :peopleNums)", 
//                   nativeQuery = true)  
//    int insertBooking(@Param("tourId") int tourId, 
//                       @Param("userId") int userId, 
//                       @Param("bookingDate") Date bookingDate, 
//                       @Param("payStatus") int payStatus, 
//                       @Param("peopleNums") int peopleNums);
//	@Query(value = "SELECT * FROM Bookings WHERE booking_id = LAST_INSERT_ID()", nativeQuery = true)
//	Bookings findLastInsertedBooking();
	
	
	@Modifying
    @Transactional
    @Query(value = "UPDATE bookings SET total_price = :totalPrice WHERE booking_id = :bookingId AND status = true", nativeQuery = true)
	int updateTotalPrice(@Param("totalPrice") float totalPrice, @Param("bookingId") Long bookingId);
    
	@Transactional
    @Modifying
    @Query(value = "SELECT * FROM  Bookings   WHERE user_id = :userId AND status = true", nativeQuery = true)
    Optional<List<Bookings>> getUserByBookingId(@Param("userId") Long userId);
    
	//hiện danh sách lên bảng trên trang order(đơn hàng của bạn)
	@Transactional
	@Modifying
	@Query(value = "SELECT b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	        + "SUM(tb.quantity) AS totalQuantity, "
	        + "GROUP_CONCAT(CONCAT(t.name, ': ', tb.quantity) SEPARATOR ', ') AS ticketDetails, "
	        + "b.total_price, tours.start_date, tours.departure, p.payment_id "
	        + "FROM bookings b "
	        + "LEFT JOIN payments p ON p.booking_id = b.booking_id "
	        + "LEFT JOIN ticketbooking tb ON tb.booking_id = b.booking_id "
	        + "LEFT JOIN ticket t ON t.ticket_id = tb.ticket_id "
	        + "JOIN tours tours ON tours.tour_id = b.tour_id "
	        + "WHERE b.user_id = :userId AND b.status = true "
	        + "GROUP BY b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	        + "b.total_price, tours.start_date, tours.departure, p.payment_id "
	        + "ORDER BY b.booking_date DESC", 
	        nativeQuery = true)
	List<Object[]> showDataTable(@Param("userId") Long userId);
	
	//lọc theo paid hoặc unpaid
	@Transactional
	@Modifying
	@Query(value = "SELECT b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	        + "SUM(tb.quantity) AS totalQuantity, "
	        + "GROUP_CONCAT(CONCAT(t.name, ': ', tb.quantity) SEPARATOR ', ') AS ticketDetails, "
	        + "b.total_price, tours.start_date, tours.departure, p.payment_id "
	        + "FROM bookings b "
	        + "LEFT JOIN payments p ON p.booking_id = b.booking_id "
	        + "JOIN ticketbooking tb ON tb.booking_id = b.booking_id "
	        + "JOIN ticket t ON t.ticket_id = tb.ticket_id "
	        + "JOIN tours tours ON tours.tour_id = b.tour_id "
	        + "WHERE b.user_id = :userId AND b.status = true "
	        + "AND (:paymentStatus IS NULL "  // lấy hết
	        + "OR (:paymentStatus = 1 AND p.payment_id IS NOT NULL) " 
	        + "OR (:paymentStatus = 2 AND p.payment_id IS NULL)) " 
	        + "GROUP BY b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	        + "b.total_price, tours.start_date, tours.departure, p.payment_id "
	        + "ORDER BY b.booking_date DESC", 
	        nativeQuery = true)
	List<Object[]> filterOrderPage(@Param("userId") Long userId, @Param("paymentStatus") Integer paymentStatus);

	//tìm kiếm theo khởi hành
	@Transactional
	@Modifying
	@Query(value = "SELECT b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	        + "SUM(tb.quantity) AS totalQuantity, "
	        + "GROUP_CONCAT(CONCAT(t.name, ': ', tb.quantity) SEPARATOR ', ') AS ticketDetails, "
	        + "b.total_price, tours.start_date, tours.departure, p.payment_id "
	        + "FROM bookings b "
	        + "LEFT JOIN payments p ON p.booking_id = b.booking_id "
	        + "LEFT JOIN ticketbooking tb ON tb.booking_id = b.booking_id "
	        + "LEFT JOIN ticket t ON t.ticket_id = tb.ticket_id "
	        + "JOIN tours tours ON tours.tour_id = b.tour_id "
	        + "WHERE b.user_id = :userId AND b.status = true "
	        + "AND tours.departure LIKE CONCAT('%', :searchTerm, '%') "
	        + "GROUP BY b.booking_id, b.booking_date, p.payment_method, p.payment_status, tours.tour_name, "
	        + "b.total_price, tours.start_date, tours.departure, p.payment_id "
	        + "ORDER BY b.booking_date DESC", 
	        nativeQuery = true)
	List<Object[]> searchDeparture(@Param("userId") Long userId, @Param("searchTerm") String search);




	

	 




	 
	

	

}
