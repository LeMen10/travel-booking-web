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
@Table(name = "ticket")
@Data
public class Ticket implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id")
    private Long ticketId ;

    @Column(name = "name")
    private String Name;
    
    @Column(name = "rate")
    private int Rate;
    
    @Column(name = "detail")
    private String Detail;
    
    @Column(name = "status")
    private boolean Status;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getRate() {
		return Rate;
	}

	public void setRate(int rate) {
		Rate = rate;
	}

	public String getDetail() {
		return Detail;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public Long getTicketId() {
		return ticketId;
	}

}
