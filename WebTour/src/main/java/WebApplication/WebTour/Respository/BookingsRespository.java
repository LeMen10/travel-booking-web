package WebApplication.WebTour.Respository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Bookings;
import jakarta.transaction.Transactional;

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
	
}
