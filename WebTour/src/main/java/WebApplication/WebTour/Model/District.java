package WebApplication.WebTour.Model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
@Table(name = "district")
@Data
public class District implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "district_id")
    private Long districtId;
	
	
	
    @Column(name = "name")
    private String name;
    
//    @Column(name = "province_id")
//    private int provinceId;
    @JsonManagedReference
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "province_id", nullable = true)
    private District district;
    
    @Column(name = "status")
    private boolean status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		name = name;
	}



	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}


    
}
