package WebSpring.Model;

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
    private String Name;
    
    @Column(name = "districtId")
    private int districtId;
    
    @Column(name = "status")
    private boolean Status;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		this.Status = status;
	}

	public Long getWardId() {
		return wardId;
	}
    
}
