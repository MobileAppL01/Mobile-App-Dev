package Bookington2.demo.controller;

import Bookington2.demo.dto.BannerImageDTO;
import Bookington2.demo.service.BannerImageService;
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
@RequestMapping("/api/banners")
@Tag(name = "Banner API", description = "API for managing banner images on court listing page")
public class BannerController {

    @Autowired
    private BannerImageService bannerImageService;

    // ========================================
    // PUBLIC APIs
    // ========================================

    @GetMapping
    @Operation(summary = "Get active banners", description = "Get all active banner images for display")
    public ResponseEntity<?> getActiveBanners() {
        try {
            List<BannerImageDTO> banners = bannerImageService.getActiveBanners();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", banners,
                    "count", banners.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/location/{locationId}")
    @Operation(summary = "Get active banners by location", description = "Get all active banner images for specific location")
    public ResponseEntity<?> getActiveBannersByLocation(
            @Parameter(description = "Location ID")
            @PathVariable Integer locationId) {
        try {
            List<BannerImageDTO> banners = bannerImageService.getActiveBannersByLocation(locationId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", banners,
                    "count", banners.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // ========================================
    // ADMIN APIs
    // ========================================

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all banners (Admin)", description = "Get all banners including inactive ones")
    public ResponseEntity<?> getAllBanners() {
        try {
            List<BannerImageDTO> banners = bannerImageService.getAllBanners();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", banners,
                    "count", banners.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // ========================================
    // OWNER APIs
    // ========================================

    @GetMapping("/owner/location/{locationId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Get banners for location (Owner)", description = "Get all banners for a specific location owned by the user")
    public ResponseEntity<?> getBannersByLocationForOwner(
            @Parameter(description = "Location ID")
            @PathVariable Integer locationId) {
        try {
            List<BannerImageDTO> banners = bannerImageService.getAllBannersByLocation(locationId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", banners,
                    "count", banners.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/owner/location/{locationId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Upload banner for location (Owner)", description = "Upload a new banner image for specific location")
    public ResponseEntity<?> uploadBannerForLocation(
            @Parameter(description = "Location ID")
            @PathVariable Integer locationId,
            
            @Parameter(description = "Banner image file")
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Banner title")
            @RequestParam(required = false) String title,

            @Parameter(description = "Banner description")
            @RequestParam(required = false) String description,

            @Parameter(description = "Link URL when clicked")
            @RequestParam(required = false) String linkUrl) {
        try {
            BannerImageDTO banner = bannerImageService.uploadBannerForLocation(file, title, description, linkUrl, locationId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", banner
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/owner/{bannerId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Update banner (Owner)", description = "Update banner information for owned location")
    public ResponseEntity<?> updateBannerForOwner(
            @Parameter(description = "Banner ID")
            @PathVariable Integer bannerId,

            @Parameter(description = "Banner title")
            @RequestParam(required = false) String title,

            @Parameter(description = "Banner description")
            @RequestParam(required = false) String description,

            @Parameter(description = "Link URL")
            @RequestParam(required = false) String linkUrl,

            @Parameter(description = "Active status")
            @RequestParam(required = false) Boolean isActive,

            @Parameter(description = "Display order")
            @RequestParam(required = false) Integer displayOrder) {
        try {
            BannerImageDTO banner = bannerImageService.updateBanner(bannerId, title, description, linkUrl, isActive, displayOrder);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", banner
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/owner/{bannerId}/toggle")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Toggle banner active (Owner)", description = "Toggle banner active/inactive status for owned location")
    public ResponseEntity<?> toggleBannerActiveForOwner(
            @Parameter(description = "Banner ID")
            @PathVariable Integer bannerId) {
        try {
            BannerImageDTO banner = bannerImageService.toggleActive(bannerId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", banner
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/owner/{bannerId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Delete banner (Owner)", description = "Delete a banner image for owned location")
    public ResponseEntity<?> deleteBannerForOwner(
            @Parameter(description = "Banner ID")
            @PathVariable Integer bannerId) {
        try {
            bannerImageService.deleteBanner(bannerId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Banner deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Upload banner (Admin)", description = "Upload a new banner image (global)")
    public ResponseEntity<?> uploadBanner(
            @Parameter(description = "Banner image file")
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Banner title")
            @RequestParam(required = false) String title,

            @Parameter(description = "Banner description")
            @RequestParam(required = false) String description,

            @Parameter(description = "Link URL when clicked")
            @RequestParam(required = false) String linkUrl) {
        try {
            BannerImageDTO banner = bannerImageService.uploadBanner(file, title, description, linkUrl);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", banner
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/{bannerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update banner (Admin)", description = "Update banner information")
    public ResponseEntity<?> updateBanner(
            @Parameter(description = "Banner ID")
            @PathVariable Integer bannerId,

            @Parameter(description = "Banner title")
            @RequestParam(required = false) String title,

            @Parameter(description = "Banner description")
            @RequestParam(required = false) String description,

            @Parameter(description = "Link URL")
            @RequestParam(required = false) String linkUrl,

            @Parameter(description = "Active status")
            @RequestParam(required = false) Boolean isActive,

            @Parameter(description = "Display order")
            @RequestParam(required = false) Integer displayOrder) {
        try {
            BannerImageDTO banner = bannerImageService.updateBanner(bannerId, title, description, linkUrl, isActive, displayOrder);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", banner
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/{bannerId}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle banner active (Admin)", description = "Toggle banner active/inactive status")
    public ResponseEntity<?> toggleBannerActive(
            @Parameter(description = "Banner ID")
            @PathVariable Integer bannerId) {
        try {
            BannerImageDTO banner = bannerImageService.toggleActive(bannerId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", banner
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{bannerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete banner (Admin)", description = "Delete a banner image")
    public ResponseEntity<?> deleteBanner(
            @Parameter(description = "Banner ID")
            @PathVariable Integer bannerId) {
        try {
            bannerImageService.deleteBanner(bannerId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Banner deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
