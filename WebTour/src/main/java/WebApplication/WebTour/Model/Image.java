package WebApplication.WebTour.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Table(name = "image")
@Data
public class Image {
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
    private Long imageId ;
	
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "tour_id", nullable = true)
    private Tours tours;

	@Column(name = "image_code")
    private String imageCode ;

	@Lob
    @Column(name = "image_data")
    private byte[] imageData;
	
	
	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}



	public Tours getTours() {
		return tours;
	}

	public void setTours(Tours tours) {
		this.tours = tours;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}
	
	
}
