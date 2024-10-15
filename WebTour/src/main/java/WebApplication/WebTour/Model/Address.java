package WebApplication.WebTour.Model;

import java.io.Serializable;

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
@Table(name = "address")
@Data
public class Address implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
    private Long addressId;
	
//	@Column(name = "province_id")
//    private int provinceId;
//	
//	@Column(name = "district_id ")
//    private int districtId ;
//	
//	@Column(name = "ward_id")
//    private int wardId;
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "province_id", nullable = true)
    private Province Province;
	
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "district_id", nullable = true)
    private District district;
	
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "ward_id", nullable = true)
    private Ward ward;
	
    @Column(name = "detail")
    private String detail;
    
    public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	@Column(name = "status")
    private boolean status;



	public Province getProvince() {
		return Province;
	}

	public void setProvince(Province province) {
		Province = province;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Ward getWard() {
		return ward;
	}

	public void setWard(Ward ward) {
		this.ward = ward;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		detail = detail;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Long getAddressId() {
		return addressId;
	}
    
}
