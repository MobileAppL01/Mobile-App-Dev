package Bookington2.demo.service;

import Bookington2.demo.dto.owner.CreateLocationRequest;
import Bookington2.demo.dto.owner.LocationResponse;
import Bookington2.demo.dto.owner.UpdateLocationRequest;
import Bookington2.demo.entity.Location;
import Bookington2.demo.entity.User;
import Bookington2.demo.exception.AppException;
import Bookington2.demo.exception.ErrorCode;
import Bookington2.demo.repository.CourtRepository;
import Bookington2.demo.repository.LocationRepository;
import Bookington2.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerLocationService {

    private final LocationRepository locationRepository;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;

    public List<LocationResponse> getMyLocations(String ownerId) {
        return locationRepository.findAllByOwner_Id(ownerId)
                .stream()
                .map(this::toLocationResponse)
                .collect(Collectors.toList());
    }

    public LocationResponse getLocationById(Integer locationId, String ownerId) {
        Location location = locationRepository.findByIdAndOwner_Id(locationId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));
        return toLocationResponse(location);
    }

    @Transactional
    public LocationResponse createLocation(CreateLocationRequest request, String ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Location location = Location.builder()
                .owner(owner)
                .name(request.getName())
                .address(request.getAddress())
                .description(request.getDescription())
                .pricePerHour(request.getPricePerHour())
                .openTime(request.getOpenTime())
                .closeTime(request.getCloseTime())
                .image(request.getImage())
                .rating(0.0f)
                .status("OPEN")
                .build();

        location = locationRepository.save(location);
        return toLocationResponse(location);
    }

    @Transactional
    public LocationResponse updateLocation(Integer locationId, UpdateLocationRequest request, String ownerId) {
        Location location = locationRepository.findByIdAndOwner_Id(locationId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));

        if (request.getName() != null) {
            location.setName(request.getName());
        }
        if (request.getAddress() != null) {
            location.setAddress(request.getAddress());
        }
        if (request.getDescription() != null) {
            location.setDescription(request.getDescription());
        }
        if (request.getPricePerHour() != null) {
            location.setPricePerHour(request.getPricePerHour());
        }
        if (request.getOpenTime() != null) {
            location.setOpenTime(request.getOpenTime());
        }
        if (request.getCloseTime() != null) {
            location.setCloseTime(request.getCloseTime());
        }
        if (request.getImage() != null) {
            location.setImage(request.getImage());
        }
        if (request.getStatus() != null) {
            location.setStatus(request.getStatus());
        }

        location = locationRepository.save(location);
        return toLocationResponse(location);
    }

    @Transactional
    public void deleteLocation(Integer locationId, String ownerId) {
        Location location = locationRepository.findByIdAndOwner_Id(locationId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));

        locationRepository.delete(location);
    }

    private LocationResponse toLocationResponse(Location location) {
        int courtCount = courtRepository.countByLocation_IdAndDeletedFalse(location.getId());

        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .description(location.getDescription())
                .image(location.getImage())
                .ratingAvg(location.getRating())
                .pricePerHour(location.getPricePerHour())
                .openTime(location.getOpenTime())
                .closeTime(location.getCloseTime())
                .status(location.getStatus())
                .courtCount(courtCount)
                .build();
    }
}

