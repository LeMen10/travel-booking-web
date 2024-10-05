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
@Table(name = "district")
@Data
public class District implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "district_id")
    private Long districtId;

    @Column(name = "name")
    private String Name;
    
    @Column(name = "province_id")
    private int provinceId;
    
    @Column(name = "status")
    private boolean Status;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		this.Status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getDistrictId() {
		return districtId;
	}
    
}
