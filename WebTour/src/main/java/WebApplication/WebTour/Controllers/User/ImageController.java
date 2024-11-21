package WebApplication.WebTour.Controllers.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import WebApplication.WebTour.Model.Image;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.ImageRepository;
import WebApplication.WebTour.Respository.ToursRepository;

@Controller
public class ImageController {
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	ToursRepository toursRepository;

	@GetMapping("/image/{imageId}")
	public ResponseEntity<byte[]> getImage(@PathVariable("imageId") Long imageId) {
		Optional<Image> image = imageRepository.findById(imageId);
	    if (image.isPresent()) {
	        byte[] imageData = image.get().getImageData();
	        String imageCode = image.get().getImageCode();
	        
	        // Kiểm tra xem imageCode có chứa định dạng ảnh không
	        MediaType mediaType = MediaType.IMAGE_JPEG; // Giả sử mặc định là JPEG
	        if (imageCode != null && imageCode.toLowerCase().endsWith(".png")) {
	            mediaType = MediaType.IMAGE_PNG;
	        } else if (imageCode != null && imageCode.toLowerCase().endsWith(".gif")) {
	            mediaType = MediaType.IMAGE_GIF;
	        }

	        return ResponseEntity.ok().contentType(mediaType).body(imageData);
	    }
	    return ResponseEntity.notFound().build();
	}
	
	// Lưu backgroundImage và otherImages
    @PostMapping("/image/upload/{tourId}")
    public ResponseEntity<String> uploadImages(
    		@PathVariable Long tourId,
            @RequestPart("backgroundImage") MultipartFile backgroundImage,
            @RequestPart("otherImages") List<MultipartFile> otherImages) {
        try {
        	// Lấy thông tin tour từ database
        	Optional<Tours> tour = toursRepository.findById(tourId);
                    
            // Lưu backgroundImage
            Image backgroundImg = new Image();
            System.out.println(tourId+"aaaaaaaaaa");
            backgroundImg.setImageData(backgroundImage.getBytes());
            backgroundImg.setImageCode(backgroundImage.getOriginalFilename());
            backgroundImg.setTours(tour.get());
            backgroundImg.setBackground(true); // Đánh dấu là ảnh nền
            imageRepository.save(backgroundImg);

            // Lưu otherImages
            for (MultipartFile file : otherImages) {
                Image img = new Image();
                img.setImageData(file.getBytes());
                img.setImageCode(file.getOriginalFilename());
                img.setTours(tour.get());
                img.setBackground(false); // Không phải ảnh nền
                imageRepository.save(img);
            }

            return ResponseEntity.ok("Images uploaded successfully for tour ID: " + tourId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload images: " + e.getMessage());
        }
    }
}
