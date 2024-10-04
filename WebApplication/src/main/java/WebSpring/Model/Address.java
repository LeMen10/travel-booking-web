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
@Table(name = "address")
@Data
public class Address implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
    private Long addressId;
	
	@Column(name = "province_id")
    private int provinceId;
	
	@Column(name = "district_id ")
    private int districtId ;
	
	@Column(name = "ward_id")
    private int wardId;
	
    @Column(name = "detail")
    private String Detail;
    
    @Column(name = "status")
    private boolean Status;

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public int getWardId() {
		return wardId;
	}

	public void setWardId(int wardId) {
		this.wardId = wardId;
	}

	public String getDetail() {
		return Detail;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		this.Status = status;
	}

	public Long getAddressId() {
		return addressId;
	}
    
}
