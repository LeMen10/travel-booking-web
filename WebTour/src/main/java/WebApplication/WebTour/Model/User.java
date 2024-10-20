package WebApplication.WebTour.Model;

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
@Table(name = "user")
@Data
public class User {
	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
    private Long user_id;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "role_id", nullable = true)
    private Role role;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "email")
    private String email;
    
    
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "status")
    private boolean status = true;
    
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "address", nullable = true)
    private Address address;

    
	public User() {
		super();
	}

	public User(Role role, String fullName, String email, String phone, String gender, Address address) {
		super();
		this.role = role;
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.gender = gender;
		this.address = address;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}


	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", role=" + role + ", fullName=" + fullName + ", email=" + email
				+ ", phone=" + phone + ", gender=" + gender + ", status=" + status + ", address=" + address + "]";
	}
        
    
}
