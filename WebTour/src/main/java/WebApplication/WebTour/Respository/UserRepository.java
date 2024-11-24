package WebApplication.WebTour.Respository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import WebApplication.WebTour.Model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Query("SELECT u FROM User u WHERE u.role.roleId = :roleId")
	Page<Object[]> findByRoleId(@Param("roleId") int roleId, Pageable pageable);
	
	@Query("SELECT u, COUNT(b) FROM User u LEFT JOIN u.bookings b WHERE  u.role.roleId = 3 GROUP BY u.user_id ORDER BY COUNT(b) ASC")
	List<Object[]> findAllUsersOrderedByBookingsAsc();

	@Query("SELECT u, COUNT(b) FROM User u LEFT JOIN u.bookings b WHERE  u.role.roleId = 3 GROUP BY u.user_id ORDER BY COUNT(b) DESC")
	List<Object[]> findAllUsersOrderedByBookingsDesc();
	
	@Query("SELECT u FROM User u WHERE u.role.roleId = 2 AND u.status = true")
	List<User> findAllEmployee();
	
}