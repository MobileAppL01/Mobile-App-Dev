package Bookington2.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Upload image to Cloudinary
     * @param file MultipartFile to upload
     * @param folder Folder name in Cloudinary (e.g., "avatars", "courts", "locations")
     * @return Map containing upload result (secure_url, public_id, etc.)
     */
    public Map uploadImage(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Max file size: 10MB
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB");
        }

        String publicId = folder + "/" + UUID.randomUUID();

        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "folder", folder,
                        "resource_type", "image"
                )
        );

        return result;
    }

    /**
     * Upload avatar image
     */
    public String uploadAvatar(MultipartFile file) throws IOException {
        Map result = uploadImage(file, "avatars");
        return (String) result.get("secure_url");
    }

    /**
     * Upload court image
     */
    public String uploadCourtImage(MultipartFile file) throws IOException {
        Map result = uploadImage(file, "courts");
        return (String) result.get("secure_url");
    }

    /**
     * Upload location image
     */
    public String uploadLocationImage(MultipartFile file) throws IOException {
        Map result = uploadImage(file, "locations");
        return (String) result.get("secure_url");
    }

    /**
     * Delete image from Cloudinary by public_id
     */
    public boolean deleteImage(String publicId) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            return false;
        }
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return "ok".equals(result.get("result"));
    }

    /**
     * Extract public_id from Cloudinary URL
     */
    public String extractPublicId(String url) {
        if (url == null || !url.contains("cloudinary.com")) {
            return null;
        }
        // URL format: https://res.cloudinary.com/{cloud_name}/image/upload/{version}/{public_id}.{format}
        try {
            String[] parts = url.split("/upload/");
            if (parts.length > 1) {
                String pathWithFormat = parts[1];
                // Remove version if present (v1234567890/)
                if (pathWithFormat.matches("v\\d+/.*")) {
                    pathWithFormat = pathWithFormat.substring(pathWithFormat.indexOf("/") + 1);
                }
                // Remove file extension
                int lastDot = pathWithFormat.lastIndexOf(".");
                if (lastDot > 0) {
                    return pathWithFormat.substring(0, lastDot);
                }
                return pathWithFormat;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
