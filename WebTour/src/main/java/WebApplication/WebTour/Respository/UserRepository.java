package WebApplication.WebTour.Respository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import WebApplication.WebTour.Model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Transactional
	@Query(value = "SELECT * FROM user WHERE role_id = :roleId", nativeQuery = true)
    List<User> findByRoleId(@Param("roleId") int roleId);
	
	@Query("SELECT u, COUNT(b) FROM User u LEFT JOIN u.bookings b WHERE  u.role.roleId = 3 GROUP BY u.user_id ORDER BY COUNT(b) ASC")
	List<Object[]> findAllUsersOrderedByBookingsAsc();

	@Query("SELECT u, COUNT(b) FROM User u LEFT JOIN u.bookings b WHERE  u.role.roleId = 3 GROUP BY u.user_id ORDER BY COUNT(b) DESC")
	List<Object[]> findAllUsersOrderedByBookingsDesc();
	
}