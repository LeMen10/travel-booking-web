package WebSpring.Model;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bookings")
@Data
public class Bookings implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id ")
    private Long bookingId ;
	
	@Column(name = "tour_id ")
    private int tourId; 

    @Column(name = "customer_id")
    private int customerId;
    
    @Column(name = "booking_date")
    private Date bookingDate;

    @Column(name = "status")
    private boolean Status;
    
    @Column(name = "pay_status")
    private int payStatus;
    
    @Column(name = "people_nums")
    private int peopleNums;

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public int getPeopleNums() {
		return peopleNums;
	}

	public void setPeopleNums(int peopleNums) {
		this.peopleNums = peopleNums;
	}

	public Long getBookingId() {
		return bookingId;
	}
    
    
}
