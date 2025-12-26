package Bookington2.demo.service;

import Bookington2.demo.dto.owner.CourtResponse;
import Bookington2.demo.dto.owner.CreateCourtRequest;
import Bookington2.demo.entity.Court;
import Bookington2.demo.entity.Location;
import Bookington2.demo.enums.CourtStatus;
import Bookington2.demo.exception.AppException;
import Bookington2.demo.exception.ErrorCode;
import Bookington2.demo.repository.CourtRepository;
import Bookington2.demo.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerCourtService {

    private final CourtRepository courtRepository;
    private final LocationRepository locationRepository;

    public List<CourtResponse> getCourtsByLocation(Integer locationId, String ownerId) {
        // Verify owner owns this location
        if (!locationRepository.existsByIdAndOwner_Id(locationId, ownerId)) {
            throw new AppException(ErrorCode.LOCATION_NOT_FOUND);
        }

        return courtRepository.findAllByLocation_IdAndDeletedFalse(locationId)
                .stream()
                .map(this::toCourtResponse)
                .collect(Collectors.toList());
    }

    public List<CourtResponse> getAllMyCourts(String ownerId) {
        return courtRepository.findAllByLocation_Owner_IdAndDeletedFalse(ownerId)
                .stream()
                .map(this::toCourtResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourtResponse createCourt(Integer locationId, CreateCourtRequest request, String ownerId) {
        Location location = locationRepository.findByIdAndOwner_Id(locationId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));

        Court court = Court.builder()
                .location(location)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : CourtStatus.ACTIVE)
                .deleted(false)
                .build();

        court = courtRepository.save(court);
        return toCourtResponse(court);
    }

    @Transactional
    public CourtResponse updateCourtStatus(Integer courtId, CourtStatus status, String ownerId) {
        Court court = courtRepository.findByIdAndLocation_Owner_IdAndDeletedFalse(courtId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.COURT_NOT_FOUND));

        court.setStatus(status);
        court = courtRepository.save(court);
        return toCourtResponse(court);
    }

    @Transactional
    public void deleteCourt(Integer courtId, String ownerId) {
        Court court = courtRepository.findByIdAndLocation_Owner_IdAndDeletedFalse(courtId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.COURT_NOT_FOUND));

        // Soft delete
        court.setDeleted(true);
        courtRepository.save(court);
    }

    private CourtResponse toCourtResponse(Court court) {
        return CourtResponse.builder()
                .id(court.getId())
                .name(court.getName())
                .status(court.getStatus())
                .locationId(court.getLocation().getId())
                .locationName(court.getLocation().getName())
                .build();
    }
}

