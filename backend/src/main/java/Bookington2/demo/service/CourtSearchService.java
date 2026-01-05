package Bookington2.demo.service;

import Bookington2.demo.dto.CourtSearchDTO;
import Bookington2.demo.entity.Court;
import Bookington2.demo.entity.Location;
import Bookington2.demo.repository.CourtRepository;
import Bookington2.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourtSearchService {

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<CourtSearchDTO> searchCourts(String keyword, String province, String district, 
                                           Integer minPrice, Integer maxPrice, int page, int size) {
        
        // Create pageable object
        Pageable pageable = PageRequest.of(page, size);
        
        // Search locations with filters
        List<Location> locations = locationRepository.findLocationsWithFilters(
            province, district, minPrice, maxPrice, pageable);
        
        // Get courts from these locations
        List<Court> courts = locations.stream()
            .flatMap(location -> courtRepository.findByLocationAndDeletedFalse(location).stream())
            .collect(Collectors.toList());
        
        // Apply keyword filter if provided
        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchKeyword = keyword.toLowerCase();
            courts = courts.stream()
                .filter(court -> {
                    Location location = court.getLocation();
                    return court.getName().toLowerCase().contains(searchKeyword) ||
                           location.getName().toLowerCase().contains(searchKeyword) ||
                           location.getAddress().toLowerCase().contains(searchKeyword) ||
                           location.getDescription().toLowerCase().contains(searchKeyword);
                })
                .collect(Collectors.toList());
        }
        
        // Apply additional price filtering if needed (in case repository doesn't support it)
        if (minPrice != null || maxPrice != null) {
            courts = courts.stream()
                .filter(court -> {
                    Integer price = court.getLocation().getPricePerHour();
                    if (minPrice != null && price < minPrice) return false;
                    if (maxPrice != null && price > maxPrice) return false;
                    return true;
                })
                .collect(Collectors.toList());
        }
        
        // Convert to DTOs
        return courts.stream()
            .map(this::toCourtSearchDTO)
            .collect(Collectors.toList());
    }

    public List<CourtSearchDTO> searchByPriceRange(Integer minPrice, Integer maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        List<Location> locations = locationRepository.findLocationsWithFilters(
            null, null, minPrice, maxPrice, pageable);
        
        List<Court> courts = locations.stream()
            .flatMap(location -> courtRepository.findByLocationAndDeletedFalse(location).stream())
            .collect(Collectors.toList());
        
        return courts.stream()
            .map(this::toCourtSearchDTO)
            .collect(Collectors.toList());
    }

    public List<CourtSearchDTO> searchByProvince(String province, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        List<Location> locations = locationRepository.findLocationsWithFilters(
            province, null, null, null, pageable);
        
        List<Court> courts = locations.stream()
            .flatMap(location -> courtRepository.findByLocationAndDeletedFalse(location).stream())
            .collect(Collectors.toList());
        
        return courts.stream()
            .map(this::toCourtSearchDTO)
            .collect(Collectors.toList());
    }

    public List<CourtSearchDTO> searchByDistrict(String district, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        List<Location> locations = locationRepository.findLocationsWithFilters(
            null, district, null, null, pageable);
        
        List<Court> courts = locations.stream()
            .flatMap(location -> courtRepository.findByLocationAndDeletedFalse(location).stream())
            .collect(Collectors.toList());
        
        return courts.stream()
            .map(this::toCourtSearchDTO)
            .collect(Collectors.toList());
    }

    public List<CourtSearchDTO> searchByProvinceAndDistrict(String province, String district, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        List<Location> locations = locationRepository.findLocationsWithFilters(
            province, district, null, null, pageable);
        
        List<Court> courts = locations.stream()
            .flatMap(location -> courtRepository.findByLocationAndDeletedFalse(location).stream())
            .collect(Collectors.toList());
        
        return courts.stream()
            .map(this::toCourtSearchDTO)
            .collect(Collectors.toList());
    }

    public List<String> getAvailableProvinces() {
        // Extract provinces from addresses
        return locationRepository.findAll().stream()
            .map(location -> {
                String address = location.getAddress();
                if (address != null && address.contains(",")) {
                    String[] parts = address.split(",");
                    return parts[parts.length - 1].trim();
                }
                return "";
            })
            .filter(province -> !province.isEmpty())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    public List<String> getDistrictsByProvince(String province) {
        // Extract districts for a specific province
        return locationRepository.findAll().stream()
            .filter(location -> {
                String address = location.getAddress();
                return address != null && address.toLowerCase().contains(province.toLowerCase());
            })
            .map(location -> {
                String address = location.getAddress();
                String[] parts = address.split(",");
                if (parts.length >= 2) {
                    return parts[parts.length - 2].trim();
                }
                return "";
            })
            .filter(district -> !district.isEmpty())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    public List<String> getAllDistricts() {
        // Extract all districts
        return locationRepository.findAll().stream()
            .map(location -> {
                String address = location.getAddress();
                String[] parts = address.split(",");
                if (parts.length >= 2) {
                    return parts[parts.length - 2].trim();
                }
                return "";
            })
            .filter(district -> !district.isEmpty())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    public CourtSearchDTO getCourtDetails(Integer courtId) {
        Court court = courtRepository.findByIdAndDeletedFalse(courtId)
            .orElseThrow(() -> new RuntimeException("Court not found with id: " + courtId));
        
        return toCourtSearchDTO(court);
    }

    public List<CourtSearchDTO> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        List<Location> locations = locationRepository.searchByAddress(keyword);
        
        List<Court> courts = locations.stream()
            .flatMap(location -> courtRepository.findByLocationAndDeletedFalse(location).stream())
            .collect(Collectors.toList());
        
        // Also search by court name
        List<Court> courtsByName = courtRepository.findAll().stream()
            .filter(court -> court.getName().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
        
        // Combine and remove duplicates
        courts.addAll(courtsByName);
        List<Court> uniqueCourts = courts.stream()
            .distinct()
            .collect(Collectors.toList());
        
        return uniqueCourts.stream()
            .map(this::toCourtSearchDTO)
            .collect(Collectors.toList());
    }

    private CourtSearchDTO toCourtSearchDTO(Court court) {
        Location location = court.getLocation();
        String address = location.getAddress();
        
        // Extract province and district from address
        String[] addressParts = address.split(",");
        String district = addressParts.length > 2 ? addressParts[addressParts.length - 2].trim() : "";
        String province = addressParts.length > 1 ? addressParts[addressParts.length - 1].trim() : "";
        
        return CourtSearchDTO.builder()
            .id(court.getId())
            .name(court.getName())
            .address(address)
            .description(location.getDescription())
            .image(location.getImage())
            .rating(location.getRating())
            .pricePerHour(location.getPricePerHour())
            .openTime(location.getOpenTime())
            .closeTime(location.getCloseTime())
            .status(location.getStatus())
            .locationId(location.getId())
            .province(province)
            .district(district)
            .build();
    }
}