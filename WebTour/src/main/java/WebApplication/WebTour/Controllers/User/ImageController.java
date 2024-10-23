package WebApplication.WebTour.Controllers.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import WebApplication.WebTour.Model.Image;
import WebApplication.WebTour.Respository.ImageRepository;

@Controller
public class ImageController {
	@Autowired
	ImageRepository imageRepository;

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

}
