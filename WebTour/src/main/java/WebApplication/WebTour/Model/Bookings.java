package WebApplication.WebTour.Model;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "bookings")
@Data
public class Bookings implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
    private Long bookingId ;
	
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "tour_id", nullable = true)
    private Tours tour; 

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
    
    @Column(name = "booking_date")
    private Date bookingDate;

    @Column(name = "status")
    private boolean status = true;
    
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "payment_id", nullable = true)
    private Payments payment;
    
    @Column(name = "people_nums")
    private int peopleNums;

    @Column(name = "total_price")
    private float totalPrice;


	public Tours getTour() {
		return tour;
	}

	public void setTour(Tours tour) {
		this.tour = tour;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
		this.status = status;
	}

	public Payments getPayment() {
		return payment;
	}

	public void setPayment(Payments payment) {
		this.payment = payment;
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

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}



    
}
