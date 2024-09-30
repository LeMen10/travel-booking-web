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
@Table(name = "customers")
@Data
public class Customer implements Serializable{

		private static final long serialVersionUID = 1L;

		@Id
	 	@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "customer_id")
	    private Long customerId;

	    @Column(name = "full_name")
	    private String fullName;
	    
	    @Column(name = "phone")
	    private String Phone;
	    
	    @Column(name="email")
	    private String Email;
	    
	    @Column(name="address")
	    private int Address;
	    
	    @Column(name="points")
	    private int Points;

		public Long getCustomerId() {
			return customerId;
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getPhone() {
			return Phone;
		}

		public void setPhone(String phone) {
			Phone = phone;
		}

		public String getEmail() {
			return Email;
		}

		public void setEmail(String email) {
			Email = email;
		}

		public int getAddress() {
			return Address;
		}

		public void setAddress(Integer address) {
			if(address == null) return;
			Address = address;
		}

		public int getPoints() {
			return Points;
		}

		public void setPoints(Integer points) {
			if(points == null) return;
			Points = points;
		}
	    
	    
}
