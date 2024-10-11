package WebApplication.WebTour.Model;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

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

    @Column(name = "user_id")
    private int userId;
    
    @Column(name = "booking_date")
    private Date bookingDate;

    @Column(name = "status")
    private boolean status;
    
    @Column(name = "pay_status")
    private int payStatus;
    
    @Column(name = "people_nums")
    private int peopleNums;

	

	public int getTourId() {
		return tourId;
	}

	public void setTourId(int tourId) {
		this.tourId = tourId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		status = status;
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
