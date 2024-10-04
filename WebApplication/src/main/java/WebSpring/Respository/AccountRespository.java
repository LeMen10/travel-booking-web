package WebSpring.Respository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Account;

@Repository
public interface AccountRespository extends JpaRepository<Account, Long>{
	Account findByUserNameAndPassword(String userName, String Password);
}