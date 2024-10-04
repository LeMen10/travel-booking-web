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
	    private Long customer_id;

	    @Column(name = "full_name")
	    private String full_name;
	    
	    @Column(name = "phone")
	    private String phone;
	    
	    @Column(name="email")
	    private String email;
	    
	    @Column(name="address")
	    private int address;
	    
	    @Column(name="points")
	    private int Points;
	    
	    @Column(name = "status")
	    private boolean Status;

		public boolean isStatus() {
			return Status;
		}

		public void setStatus(boolean status) {
			Status = status;
		}

		public Long getCustomerId() {
			return customer_id;
		}

		public String getFull_name() {
			return full_name;
		}

		public void setFull_name(String FullName) {
			this.full_name = FullName;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String Phone) {
			phone = Phone;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String Email) {
			email = Email;
		}

		public int getAddress() {
			return address;
		}

		public void setAddress(Integer Address) {
			if(Address == null) return;
			address = Address;
		}

		public int getPoints() {
			return points;
		}

		public void setPoints(Integer Points) {
			if(Points == null) return;
			points = Points;
		}
	    
	    
}
