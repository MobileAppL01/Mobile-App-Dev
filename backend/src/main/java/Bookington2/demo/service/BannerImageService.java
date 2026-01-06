package Bookington2.demo.service;

import Bookington2.demo.dto.BannerImageDTO;
import Bookington2.demo.entity.BannerImage;
import Bookington2.demo.entity.Location;
import Bookington2.demo.repository.BannerImageRepository;
import Bookington2.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BannerImageService {

    @Autowired
    private BannerImageRepository bannerImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private LocationRepository locationRepository;

    /**
     * Get all active banners for display (public)
     */
    public List<BannerImageDTO> getActiveBanners() {
        return bannerImageRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all banners (admin)
     */
    public List<BannerImageDTO> getAllBanners() {
        return bannerImageRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Upload new banner (admin only - global banner)
     */
    @Transactional
    public BannerImageDTO uploadBanner(MultipartFile file, String title, String description, String linkUrl) throws IOException {
        return uploadBannerForLocation(file, title, description, linkUrl, null);
    }

    /**
     * Upload new banner for specific location (owner only)
     */
    @Transactional
    public BannerImageDTO uploadBannerForLocation(MultipartFile file, String title, String description, String linkUrl, Integer locationId) throws IOException {
        // Validate location if provided
        Location location = null;
        if (locationId != null) {
            location = locationRepository.findById(locationId)
                    .orElseThrow(() -> new RuntimeException("Location not found"));
        }

        // Upload to Cloudinary
        Map result = cloudinaryService.uploadImage(file, "banners");
        String publicId = (String) result.get("public_id");
        String secureUrl = (String) result.get("secure_url");

        // Get next display order
        Integer maxOrder = locationId != null ? 
                bannerImageRepository.findMaxDisplayOrderByLocationId(locationId) :
                bannerImageRepository.findMaxDisplayOrder();
        int nextOrder = (maxOrder != null ? maxOrder : 0) + 1;

        // Save to database
        BannerImage banner = BannerImage.builder()
                .title(title)
                .description(description)
                .publicId(publicId)
                .secureUrl(secureUrl)
                .linkUrl(linkUrl)
                .location(location)
                .displayOrder(nextOrder)
                .isActive(true)
                .build();

        BannerImage saved = bannerImageRepository.save(banner);
        return toDTO(saved);
    }

    /**
     * Update banner info (admin only)
     */
    @Transactional
    public BannerImageDTO updateBanner(Integer bannerId, String title, String description, String linkUrl, Boolean isActive, Integer displayOrder) {
        BannerImage banner = bannerImageRepository.findById(bannerId)
                .orElseThrow(() -> new RuntimeException("Banner not found"));

        if (title != null) banner.setTitle(title);
        if (description != null) banner.setDescription(description);
        if (linkUrl != null) banner.setLinkUrl(linkUrl);
        if (isActive != null) banner.setIsActive(isActive);
        if (displayOrder != null) banner.setDisplayOrder(displayOrder);

        BannerImage saved = bannerImageRepository.save(banner);
        return toDTO(saved);
    }

    /**
     * Delete banner (admin only)
     */
    @Transactional
    public void deleteBanner(Integer bannerId) throws IOException {
        BannerImage banner = bannerImageRepository.findById(bannerId)
                .orElseThrow(() -> new RuntimeException("Banner not found"));

        // Delete from Cloudinary
        cloudinaryService.deleteImage(banner.getPublicId());

        // Delete from database
        bannerImageRepository.delete(banner);
    }

    /**
     * Toggle banner active status
     */
    @Transactional
    public BannerImageDTO toggleActive(Integer bannerId) {
        BannerImage banner = bannerImageRepository.findById(bannerId)
                .orElseThrow(() -> new RuntimeException("Banner not found"));

        banner.setIsActive(!banner.getIsActive());
        BannerImage saved = bannerImageRepository.save(banner);
        return toDTO(saved);
    }

    /**
     * Get banners for specific location (public)
     */
    public List<BannerImageDTO> getActiveBannersByLocation(Integer locationId) {
        return bannerImageRepository.findByLocationIdAndIsActiveTrueOrderByDisplayOrderAsc(locationId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all banners for specific location (owner)
     */
    public List<BannerImageDTO> getAllBannersByLocation(Integer locationId) {
        return bannerImageRepository.findByLocationIdOrderByDisplayOrderAsc(locationId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BannerImageDTO toDTO(BannerImage banner) {
        return BannerImageDTO.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .description(banner.getDescription())
                .publicId(banner.getPublicId())
                .secureUrl(banner.getSecureUrl())
                .linkUrl(banner.getLinkUrl())
                .displayOrder(banner.getDisplayOrder())
                .isActive(banner.getIsActive())
                .createdAt(banner.getCreatedAt())
                .locationId(banner.getLocation() != null ? banner.getLocation().getId() : null)
                .locationName(banner.getLocation() != null ? banner.getLocation().getName() : null)
                .build();
    }
}
