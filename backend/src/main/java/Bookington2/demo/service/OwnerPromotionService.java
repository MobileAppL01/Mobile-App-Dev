package Bookington2.demo.service;

import Bookington2.demo.dto.owner.CreatePromotionRequest;
import Bookington2.demo.dto.owner.PromotionResponse;
import Bookington2.demo.entity.Location;
import Bookington2.demo.entity.Promotion;
import Bookington2.demo.exception.AppException;
import Bookington2.demo.exception.ErrorCode;
import Bookington2.demo.repository.LocationRepository;
import Bookington2.demo.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerPromotionService {

    private final PromotionRepository promotionRepository;
    private final LocationRepository locationRepository;

    public List<PromotionResponse> getMyPromotions(String ownerId, Integer locationId) {
        List<Promotion> promotions;
        if (locationId != null) {
            promotions = promotionRepository.findAllByOwnerIdAndLocationId(ownerId, locationId);
        } else {
            promotions = promotionRepository.findAllByOwnerId(ownerId);
        }

        return promotions.stream()
                .map(this::toPromotionResponse)
                .collect(Collectors.toList());
    }

    public PromotionResponse getPromotionById(Integer promotionId, String ownerId) {
        Promotion promotion = promotionRepository.findByIdAndOwnerId(promotionId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));
        return toPromotionResponse(promotion);
    }

    @Transactional
    public PromotionResponse createPromotion(CreatePromotionRequest request, String ownerId) {
        // Verify owner owns this location
        Location location = locationRepository.findByIdAndOwner_Id(request.getLocationId(), ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));

        // Check if code already exists
        if (promotionRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.PROMOTION_CODE_EXISTS);
        }

        // Validate dates
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        Promotion promotion = Promotion.builder()
                .location(location)
                .code(request.getCode().toUpperCase())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .description(request.getDescription())
                .active(true)
                .build();

        promotion = promotionRepository.save(promotion);
        return toPromotionResponse(promotion);
    }

    @Transactional
    public PromotionResponse togglePromotionStatus(Integer promotionId, String ownerId) {
        Promotion promotion = promotionRepository.findByIdAndOwnerId(promotionId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        promotion.setActive(!promotion.getActive());
        promotion = promotionRepository.save(promotion);
        return toPromotionResponse(promotion);
    }

    @Transactional
    public void deletePromotion(Integer promotionId, String ownerId) {
        Promotion promotion = promotionRepository.findByIdAndOwnerId(promotionId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        promotionRepository.delete(promotion);
    }

    private PromotionResponse toPromotionResponse(Promotion promotion) {
        return PromotionResponse.builder()
                .id(promotion.getId())
                .locationId(promotion.getLocation().getId())
                .locationName(promotion.getLocation().getName())
                .code(promotion.getCode())
                .discountType(promotion.getDiscountType())
                .discountValue(promotion.getDiscountValue())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .description(promotion.getDescription())
                .active(promotion.getActive())
                .build();
    }
}

