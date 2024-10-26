package WebApplication.WebTour.Model;
import java.io.Serializable;
import java.util.Date;

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
@Table(name = "payments")
@Data
public class Payments implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
    private Long paymentId ;

	@ManyToOne
	@JoinColumn(name = "booking_id", nullable = true)
	private Bookings booking;

    @Column(name = "payment_date")
    private Date paymentDate;
    
    @Column(name = "amount")
    private float amount;

    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "paymentMethod", nullable = true)
    private Paymentmethod paymentMethod ;
    
    @Column(name = "payment_status ")
    private int paymentStatus ;
    
    @JoinColumn(name = "promotion_code", nullable = true)
    private String promotionCode ;
    
    @Column(name = "status ")
    private boolean status ;





	public Bookings getBooking() {
		return booking;
	}

	public void setBooking(Bookings booking) {
		this.booking = booking;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}


	public Paymentmethod getPaymentMethod() {
		return paymentMethod;
	}



	public void setPaymentMethod(Paymentmethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}



	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
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

	public Long getPaymentId() {
		return paymentId;
	}



	public String getPromotionCode() {
		return promotionCode;
	}



	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}


	
	
}
