package Bookington2.demo.controller;

import Bookington2.demo.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File Upload API", description = "API endpoints for uploading images to Cloudinary")
public class FileController {

    private final CloudinaryService cloudinaryService;

    public FileController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('PLAYER', 'OWNER', 'ADMIN')")
    @Operation(summary = "Upload image", description = "Upload a general image to Cloudinary")
    public ResponseEntity<?> uploadImage(
            @Parameter(description = "Image file to upload")
            @RequestParam("file") MultipartFile file,
            
            @Parameter(description = "Folder name (default: uploads)")
            @RequestParam(defaultValue = "uploads") String folder) {
        try {
            Map result = cloudinaryService.uploadImage(file, folder);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "publicId", result.get("public_id"),
                    "url", result.get("secure_url")
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/upload/avatar")
    @PreAuthorize("hasAnyRole('PLAYER', 'OWNER', 'ADMIN')")
    @Operation(summary = "Upload avatar", description = "Upload user avatar image")
    public ResponseEntity<?> uploadAvatar(
            @Parameter(description = "Avatar image file")
            @RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadAvatar(file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "url", url
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/upload/court")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "Upload court image", description = "Upload court/location image (Owner only)")
    public ResponseEntity<?> uploadCourtImage(
            @Parameter(description = "Court image file")
            @RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadCourtImage(file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "url", url
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/upload/location")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "Upload location image", description = "Upload location image (Owner only)")
    public ResponseEntity<?> uploadLocationImage(
            @Parameter(description = "Location image file")
            @RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadLocationImage(file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "url", url
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "Delete image", description = "Delete an image from Cloudinary by public_id")
    public ResponseEntity<?> deleteImage(
            @Parameter(description = "Public ID of the image to delete")
            @RequestParam String publicId) {
        try {
            boolean deleted = cloudinaryService.deleteImage(publicId);
            return ResponseEntity.ok(Map.of(
                    "success", deleted,
                    "message", deleted ? "Image deleted successfully" : "Failed to delete image"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
