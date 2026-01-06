package Bookington2.demo.controller;

import Bookington2.demo.dto.LocationImageDTO;
import Bookington2.demo.service.LocationImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@Tag(name = "Location Image API", description = "API for managing location gallery images")
public class LocationImageController {

    @Autowired
    private LocationImageService locationImageService;

    @PostMapping("/{locationId}/images")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Upload location image", description = "Upload an image to location gallery (Owner only)")
    public ResponseEntity<?> uploadImage(
            @Parameter(description = "Location ID")
            @PathVariable Integer locationId,

            @Parameter(description = "Image file")
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Set as primary image")
            @RequestParam(defaultValue = "false") boolean setPrimary) {
        try {
            LocationImageDTO image = locationImageService.uploadLocationImage(locationId, file, setPrimary);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", image
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/{locationId}/images")
    @Operation(summary = "Get location images", description = "Get all images for a location (gallery)")
    public ResponseEntity<?> getLocationImages(
            @Parameter(description = "Location ID")
            @PathVariable Integer locationId) {
        try {
            List<LocationImageDTO> images = locationImageService.getLocationImages(locationId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", images,
                    "count", images.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/{locationId}/thumbnail")
    @Operation(summary = "Get thumbnail URL", description = "Get primary/thumbnail image URL for a location")
    public ResponseEntity<?> getThumbnail(
            @Parameter(description = "Location ID")
            @PathVariable Integer locationId) {
        String url = locationImageService.getThumbnailUrl(locationId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "url", url != null ? url : ""
        ));
    }

    @PutMapping("/images/{imageId}/primary")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Set primary image", description = "Set an image as the primary/thumbnail image")
    public ResponseEntity<?> setPrimaryImage(
            @Parameter(description = "Image ID")
            @PathVariable Integer imageId) {
        try {
            LocationImageDTO image = locationImageService.setPrimaryImage(imageId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", image
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/images/{imageId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Delete image", description = "Delete an image from location gallery")
    public ResponseEntity<?> deleteImage(
            @Parameter(description = "Image ID")
            @PathVariable Integer imageId) {
        try {
            locationImageService.deleteImage(imageId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Image deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
