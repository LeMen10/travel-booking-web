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
@Table(name = "ticketbooking")
@Data
public class TicketBooking implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long ticketBookingId ;

//    @Column(name = "ticket_id")
//    private int ticketId;
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "ticket_id", nullable = true)
    private Ticket ticket;
    
    @Column(name = "booking_id")
    private int bookingId;
    
    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "status")
    private boolean status = true;



	public void setTicketBookingId(Long ticketBookingId) {
		this.ticketBookingId = ticketBookingId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}



	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Long getTicketBookingId() {
		return ticketBookingId;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}


	
	
    
}
