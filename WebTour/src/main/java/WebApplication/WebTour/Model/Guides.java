package WebApplication.WebTour.Model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "languages")
    private String languages;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) 
    private User user;

	public Long getGuideId() {
		return guideId;
	}


	public String getLanguages() {
		return languages;
	}


	public void setLanguages(String languages) {
		this.languages = languages;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
    
}
