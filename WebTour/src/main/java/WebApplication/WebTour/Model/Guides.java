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


    
    @Column(name = "languages")
    private String languages;
    

    
    @Column(name = "status")
    private boolean status;

	

	

	public Long getGuideId() {
		return guideId;
	}
    
}
