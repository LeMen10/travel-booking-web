package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Employees;

@Repository
public interface EmployeesRespository extends JpaRepository<Employees, Long>{
	
}
