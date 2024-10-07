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
@Table(name = "ward")
@Data
public class Ward implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ward_id")
    private Long wardId;

    @Column(name = "name")
    private String name;
    
    @Column(name = "districtId")
    private int districtId;
    
    @Column(name = "status")
    private boolean status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		name = name;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Long getWardId() {
		return wardId;
	}
    
}
