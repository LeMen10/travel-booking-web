package WebApplication.WebTour.Model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "guides")
@Data
public class Guides implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "guide_id")
    private Long guideId ;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String Email;
    
    @Column(name = "languages")
    private String Languages;
    
    @Column(name = "employee_id")
    private String employeeId;
    
    @Column(name = "status")
    private boolean Status;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getLanguages() {
		return Languages;
	}

	public void setLanguages(String languages) {
		Languages = languages;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public Long getGuideId() {
		return guideId;
	}
    
}
