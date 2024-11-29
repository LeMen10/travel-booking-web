package WebApplication.WebTour.DTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
public class PaymentHistoryDTO {
    private Long bookingId;
    private String momoId;
    private String captureId;
    private String fullName;
    private Date paymentDate;
    private BigDecimal totalPriceDolar; // Đổi sang BigDecimal
    private BigDecimal amount;         // Đổi sang BigDecimal
    private String paymentStatus;

    // Constructor
    public PaymentHistoryDTO(Long bookingId, String momoId, String captureId, String fullName,
                             Date paymentDate, BigDecimal totalPriceDolar, BigDecimal amount, String paymentStatus) {
        this.bookingId = bookingId;
        this.momoId = momoId;
        this.captureId = captureId;
        this.fullName = fullName;
        this.paymentDate = paymentDate;
        this.totalPriceDolar = totalPriceDolar.setScale(2, RoundingMode.HALF_UP); // Làm tròn tại đây
        this.amount = amount.setScale(0, RoundingMode.HALF_UP);                  // Làm tròn tại đây
        this.paymentStatus = paymentStatus;
    }

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public String getMomoId() {
		return momoId;
	}

	public void setMomoId(String momoId) {
		this.momoId = momoId;
	}

	public String getCaptureId() {
		return captureId;
	}

	public void setCaptureId(String captureId) {
		this.captureId = captureId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getTotalPriceDolar() {
		return totalPriceDolar;
	}

	public void setTotalPriceDolar(BigDecimal totalPriceDolar) {
		this.totalPriceDolar = totalPriceDolar;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

    
}
