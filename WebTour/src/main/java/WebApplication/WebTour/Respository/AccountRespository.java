package WebApplication.WebTour.Respository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Account;

@Repository
public interface AccountRespository extends JpaRepository<Account, Long>{
	@Query("SELECT a FROM Account a WHERE a.userName = :name AND a.status = true")
    Optional<Account> findByUserName(@Param("name") String name);
	
	@Query("SELECT a FROM Account a WHERE a.googleId = :googleId AND a.status = true")
	Optional<Account> findByGoogleId(@Param("googleId") String googleId);
	
	@Query(value = "SELECT COALESCE(COUNT(a.account_id), 0), m.month, YEAR(CURDATE()) AS year "
			+ "FROM (SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION "
			+ " SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION "
			+ " SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) AS m "
			+ "LEFT JOIN Account a ON m.month = MONTH(a.create_on) AND YEAR(a.create_on) = YEAR(CURDATE()) AND a.status = 1 "
			+ "WHERE m.month <= MONTH(CURDATE()) "
			+ "GROUP BY  m.month ORDER BY  m.month DESC LIMIT 7", nativeQuery = true)
	Optional<List<Object>> getStatisticsNewAccountLast7Months();
	
	
	@Query(value = "SELECT a FROM Account a WHERE MONTH(a.create_on) <= MONTH(CURDATE())", nativeQuery = true)
	Optional<List<Object>> getTotalAccount();
	
	@Query(value = "SELECT a FROM Account a WHERE a.status = 1 AND MONTH(a.create_on) <= MONTH(CURDATE())", nativeQuery = true)
	Optional<List<Object>> getTotalAccountActivate();
	
	@Query(value = "SELECT a FROM Account a WHERE a.status = 1 AND YAER(a.create_on) = YEAR(CURDATE())", nativeQuery = true)
	Optional<List<Object>> getTotalNewAccountInYear();
	
	@Query(value = "SELECT a FROM Account a WHERE a.status = 1 AND MONTH(a.create_on) = MONTH(CURDATE())", nativeQuery = true)
	Optional<List<Object>> getTotalNewAccountInMonth();
	
	@Query(value = "SELECT a FROM Account a WHERE a.status = 1 AND a.create_on = CURDATE", nativeQuery = true)
	Optional<List<Object>> getTotalNewAccountInDay();
	
}