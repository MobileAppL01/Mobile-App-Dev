package Bookington2.demo.service;

import Bookington2.demo.dto.LocationImageDTO;
import Bookington2.demo.entity.Location;
import Bookington2.demo.entity.LocationImage;
import Bookington2.demo.entity.User;
import Bookington2.demo.repository.LocationImageRepository;
import Bookington2.demo.repository.LocationRepository;
import Bookington2.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LocationImageService {

    @Autowired
    private LocationImageRepository locationImageRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserRepository userRepository;

    private static final int MAX_IMAGES_PER_LOCATION = 10;

    /**
     * Upload image for a location (OWNER only for their locations)
     */
    @Transactional
    public LocationImageDTO uploadLocationImage(Integer locationId, MultipartFile file, boolean setPrimary) throws IOException {
        User currentUser = getCurrentUser();
        
        // Validate location exists and user owns it
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        if (!location.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only upload images for your own locations");
        }

        // Check max images limit
        long currentCount = locationImageRepository.countByLocationId(locationId);
        if (currentCount >= MAX_IMAGES_PER_LOCATION) {
            throw new RuntimeException("Maximum " + MAX_IMAGES_PER_LOCATION + " images allowed per location");
        }

        // Upload to Cloudinary
        Map result = cloudinaryService.uploadImage(file, "locations/" + locationId);
        String publicId = (String) result.get("public_id");
        String secureUrl = (String) result.get("secure_url");

        // If this is first image or setPrimary=true, make it primary
        boolean isPrimary = setPrimary || currentCount == 0;
        
        if (isPrimary) {
            locationImageRepository.resetPrimaryForLocation(locationId);
        }

        // Save to database
        LocationImage image = LocationImage.builder()
                .location(location)
                .publicId(publicId)
                .secureUrl(secureUrl)
                .isPrimary(isPrimary)
                .build();

        LocationImage saved = locationImageRepository.save(image);

        // Update location's main image field if primary
        if (isPrimary) {
            location.setImage(secureUrl);
            locationRepository.save(location);
        }

        return toDTO(saved);
    }

    /**
     * Get all images for a location
     */
    public List<LocationImageDTO> getLocationImages(Integer locationId) {
        return locationImageRepository.findByLocationIdOrderByIsPrimaryDescCreatedAtDesc(locationId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get primary/thumbnail image for a location
     */
    public String getThumbnailUrl(Integer locationId) {
        return locationImageRepository.findByLocationIdAndIsPrimaryTrue(locationId)
                .map(LocationImage::getSecureUrl)
                .orElseGet(() -> locationImageRepository.findFirstByLocationIdOrderByCreatedAtDesc(locationId)
                        .map(LocationImage::getSecureUrl)
                        .orElse(null));
    }

    /**
     * Set an image as primary
     */
    @Transactional
    public LocationImageDTO setPrimaryImage(Integer imageId) {
        User currentUser = getCurrentUser();

        LocationImage image = locationImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Check ownership
        if (!image.getLocation().getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only modify your own location images");
        }

        // Reset all primary flags for this location
        locationImageRepository.resetPrimaryForLocation(image.getLocation().getId());

        // Set this image as primary
        image.setIsPrimary(true);
        LocationImage saved = locationImageRepository.save(image);

        // Update location's main image
        Location location = image.getLocation();
        location.setImage(image.getSecureUrl());
        locationRepository.save(location);

        return toDTO(saved);
    }

    /**
     * Delete an image
     */
    @Transactional
    public void deleteImage(Integer imageId) throws IOException {
        User currentUser = getCurrentUser();

        LocationImage image = locationImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Check ownership
        if (!image.getLocation().getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own location images");
        }

        // Delete from Cloudinary
        cloudinaryService.deleteImage(image.getPublicId());

        // If this was primary, set another image as primary
        boolean wasPrimary = image.getIsPrimary();
        Integer locationId = image.getLocation().getId();

        // Delete from database
        locationImageRepository.delete(image);

        // If was primary, find new primary
        if (wasPrimary) {
            locationImageRepository.findFirstByLocationIdOrderByCreatedAtDesc(locationId)
                    .ifPresent(newPrimary -> {
                        newPrimary.setIsPrimary(true);
                        locationImageRepository.save(newPrimary);
                        
                        Location location = newPrimary.getLocation();
                        location.setImage(newPrimary.getSecureUrl());
                        locationRepository.save(location);
                    });
        }
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    private LocationImageDTO toDTO(LocationImage image) {
        return LocationImageDTO.builder()
                .id(image.getId())
                .locationId(image.getLocation().getId())
                .publicId(image.getPublicId())
                .secureUrl(image.getSecureUrl())
                .isPrimary(image.getIsPrimary())
                .createdAt(image.getCreatedAt())
                .build();
    }
}
