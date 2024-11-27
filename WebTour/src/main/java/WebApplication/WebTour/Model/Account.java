package WebApplication.WebTour.Model;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "account")
@Data
public class Account implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
    private Long accountId;

	@OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
    
    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;
    
    @Column(name = "create_on")
    private Date createOn;
    
    @Column(name = "google_id")
    private String googleId;
    
    @Column(name = "status")
    private boolean status = true;

	public Account() {
		super();
	}

	public Account(User user, String userName, String password) {
		super();
		this.user = user;
		this.userName = userName;
		this.password = password;
	}
	
	public Account(User user, String userName, String password, Date currentDate) {
		super();
		this.user = user;
		this.userName = userName;
		this.password = password;
		createOn = currentDate;
	}

	public Long getAccountId() {
		return accountId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public Date getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}
   
	
}
