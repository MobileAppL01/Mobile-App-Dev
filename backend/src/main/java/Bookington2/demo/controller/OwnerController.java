package Bookington2.demo.controller;

import Bookington2.demo.dto.owner.*;
import Bookington2.demo.dto.request.APIResponse;
import Bookington2.demo.dto.notification.*; // Add imports for Notification DTOs
import Bookington2.demo.enums.BookingStatus;
import Bookington2.demo.enums.CourtStatus;
import Bookington2.demo.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('OWNER')")
@RequestMapping("/api/v1/owner")
@RequiredArgsConstructor
@Tag(name = "Owner API", description = "API cho chủ sân quản lý cơ sở, sân, booking và khuyến mãi")
public class OwnerController {

        private final OwnerLocationService locationService;
        private final OwnerCourtService courtService;
        private final OwnerBookingService bookingService;
        private final OwnerPromotionService promotionService;
        private final NotificationService notificationService; // Added service

        // Tạm thời hard-code ownerId, sau này sẽ lấy từ JWT Token
        private Integer getCurrentOwnerId() {
                // TODO: Lấy từ SecurityContext khi implement authentication
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                return ((UserDetailsImpl) auth.getPrincipal()).getId();
        }

        // ==================== LOCATION APIS ====================

        @GetMapping("/locations")
        @Operation(summary = "Lấy danh sách cơ sở sân của tôi", description = "Trả về tất cả cơ sở mà owner này sở hữu")
        public ResponseEntity<APIResponse<List<LocationResponse>>> getMyLocations() {
                List<LocationResponse> locations = locationService.getMyLocations(getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<List<LocationResponse>>builder()
                                .code(1000)
                                .message("Lấy danh sách sân thành công")
                                .result(locations)
                                .build());
        }

        @GetMapping("/locations/{id}")
        @Operation(summary = "Xem chi tiết cơ sở sân", description = "Lấy thông tin chi tiết của một cơ sở")
        public ResponseEntity<APIResponse<LocationResponse>> getLocationById(
                        @Parameter(description = "ID của cơ sở") @PathVariable Integer id) {
                LocationResponse location = locationService.getLocationById(id, getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<LocationResponse>builder()
                                .code(1000)
                                .message("Lấy thông tin sân thành công")
                                .result(location)
                                .build());
        }

        @PostMapping("/locations")
        @Operation(summary = "Tạo cơ sở sân mới", description = "Đăng ký một địa điểm sân mới thuộc quyền quản lý")
        public ResponseEntity<APIResponse<LocationResponse>> createLocation(
                        @Valid @RequestBody CreateLocationRequest request) {
                LocationResponse location = locationService.createLocation(request, getCurrentOwnerId());
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(APIResponse.<LocationResponse>builder()
                                                .code(1000)
                                                .message("Tạo sân thành công")
                                                .result(location)
                                                .build());
        }

        @PutMapping("/locations/{id}")
        @Operation(summary = "Cập nhật thông tin sân", description = "Sửa giá tiền, giờ mở cửa, mô tả...")
        public ResponseEntity<APIResponse<LocationResponse>> updateLocation(
                        @Parameter(description = "ID của cơ sở") @PathVariable Integer id,
                        @RequestBody UpdateLocationRequest request) {
                LocationResponse location = locationService.updateLocation(id, request, getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<LocationResponse>builder()
                                .code(1000)
                                .message("Cập nhật sân thành công")
                                .result(location)
                                .build());
        }

        @DeleteMapping("/locations/{id}")
        @Operation(summary = "Xóa cơ sở sân", description = "Xóa một cơ sở và tất cả sân con bên trong")
        public ResponseEntity<APIResponse<Void>> deleteLocation(
                        @Parameter(description = "ID của cơ sở") @PathVariable Integer id) {
                locationService.deleteLocation(id, getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<Void>builder()
                                .code(1000)
                                .message("Xóa sân thành công")
                                .build());
        }

        // ==================== COURT APIS ====================

        @GetMapping("/locations/{locationId}/courts")
        @Operation(summary = "Lấy danh sách sân con của cơ sở", description = "Lấy tất cả sân con trong một cơ sở")
        public ResponseEntity<APIResponse<List<CourtResponse>>> getCourtsByLocation(
                        @Parameter(description = "ID của cơ sở") @PathVariable Integer locationId) {
                List<CourtResponse> courts = courtService.getCourtsByLocation(locationId, getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<List<CourtResponse>>builder()
                                .code(1000)
                                .message("Lấy danh sách sân con thành công")
                                .result(courts)
                                .build());
        }

        @GetMapping("/courts")
        @Operation(summary = "Lấy tất cả sân con của tôi", description = "Lấy tất cả sân con của owner")
        public ResponseEntity<APIResponse<List<CourtResponse>>> getAllMyCourts() {
                List<CourtResponse> courts = courtService.getAllMyCourts(getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<List<CourtResponse>>builder()
                                .code(1000)
                                .message("Lấy danh sách sân con thành công")
                                .result(courts)
                                .build());
        }

        @PostMapping("/locations/{locationId}/courts")
        @Operation(summary = "Thêm sân con vào cơ sở", description = "Thêm một sân cụ thể vào cơ sở")
        public ResponseEntity<APIResponse<CourtResponse>> createCourt(
                        @Parameter(description = "ID của cơ sở") @PathVariable Integer locationId,
                        @Valid @RequestBody CreateCourtRequest request) {
                CourtResponse court = courtService.createCourt(locationId, request, getCurrentOwnerId());
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(APIResponse.<CourtResponse>builder()
                                                .code(1000)
                                                .message("Thêm sân con thành công")
                                                .result(court)
                                                .build());
        }

        @PatchMapping("/courts/{courtId}/status")
        @Operation(summary = "Cập nhật trạng thái sân", description = "Dùng khi sân bị hỏng cần bảo trì hoặc mở lại")
        public ResponseEntity<APIResponse<CourtResponse>> updateCourtStatus(
                        @Parameter(description = "ID của sân con") @PathVariable Integer courtId,
                        @Parameter(description = "Trạng thái mới: ACTIVE hoặc MAINTENANCE") @RequestParam CourtStatus status) {
                CourtResponse court = courtService.updateCourtStatus(courtId, status, getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<CourtResponse>builder()
                                .code(1000)
                                .message("Cập nhật trạng thái sân thành công")
                                .result(court)
                                .build());
        }

        @DeleteMapping("/courts/{courtId}")
        @Operation(summary = "Xóa sân con (Soft delete)", description = "Xóa mềm một sân con")
        public ResponseEntity<APIResponse<Void>> deleteCourt(
                        @Parameter(description = "ID của sân con") @PathVariable Integer courtId) {
                courtService.deleteCourt(courtId, getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<Void>builder()
                                .code(1000)
                                .message("Xóa sân con thành công")
                                .build());
        }

        // ==================== BOOKING APIS ====================

        @GetMapping("/bookings")
        @Operation(summary = "Xem danh sách đặt sân", description = "Lấy danh sách booking với các bộ lọc")
        public ResponseEntity<APIResponse<List<BookingResponse>>> getBookings(
                        @Parameter(description = "Lọc theo cơ sở") @RequestParam(required = false) Integer locationId,
                        @Parameter(description = "Lọc theo ngày (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        @Parameter(description = "Lọc theo trạng thái") @RequestParam(required = false) BookingStatus status) {
                List<BookingResponse> bookings = bookingService.getBookings(getCurrentOwnerId(), locationId, date,
                                status);
                return ResponseEntity.ok(APIResponse.<List<BookingResponse>>builder()
                                .code(1000)
                                .message("Lấy danh sách đặt sân thành công")
                                .result(bookings)
                                .build());
        }

        @GetMapping("/bookings/{id}")
        @Operation(summary = "Xem chi tiết booking", description = "Lấy thông tin chi tiết của một booking")
        public ResponseEntity<APIResponse<BookingResponse>> getBookingById(
                        @Parameter(description = "ID của booking") @PathVariable Integer id) {
                BookingResponse booking = bookingService.getBookingById(id, getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<BookingResponse>builder()
                                .code(1000)
                                .message("Lấy thông tin booking thành công")
                                .result(booking)
                                .build());
        }

        @PutMapping("/bookings/{id}/status")
        @Operation(summary = "Duyệt hoặc Hủy lịch đặt", description = "Chủ sân chấp nhận hoặc từ chối lịch đặt")
        public ResponseEntity<APIResponse<BookingResponse>> updateBookingStatus(
                        @Parameter(description = "ID của booking") @PathVariable Integer id,
                        @Valid @RequestBody UpdateBookingStatusRequest request) {
                BookingResponse booking = bookingService.updateBookingStatus(id, request.getStatus(),
                                getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<BookingResponse>builder()
                                .code(1000)
                                .message("Cập nhật trạng thái booking thành công")
                                .result(booking)
                                .build());
        }

        // ==================== PROMOTION APIS ====================

        @GetMapping("/promotions")
        @Operation(summary = "Lấy danh sách khuyến mãi", description = "Lấy tất cả khuyến mãi của owner")
        public ResponseEntity<APIResponse<List<PromotionResponse>>> getPromotions(
                        @Parameter(description = "Lọc theo cơ sở") @RequestParam(required = false) Integer locationId) {
                List<PromotionResponse> promotions = promotionService.getMyPromotions(getCurrentOwnerId(), locationId);
                return ResponseEntity.ok(APIResponse.<List<PromotionResponse>>builder()
                                .code(1000)
                                .message("Lấy danh sách khuyến mãi thành công")
                                .result(promotions)
                                .build());
        }

        @GetMapping("/promotions/{id}")
        @Operation(summary = "Xem chi tiết khuyến mãi", description = "Lấy thông tin chi tiết của một khuyến mãi")
        public ResponseEntity<APIResponse<PromotionResponse>> getPromotionById(
                        @Parameter(description = "ID của khuyến mãi") @PathVariable Integer id) {
                PromotionResponse promotion = promotionService.getPromotionById(id, getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<PromotionResponse>builder()
                                .code(1000)
                                .message("Lấy thông tin khuyến mãi thành công")
                                .result(promotion)
                                .build());
        }

        @PostMapping("/promotions")
        @Operation(summary = "Tạo khuyến mãi mới", description = "Tạo mã khuyến mãi cho một cơ sở")
        public ResponseEntity<APIResponse<PromotionResponse>> createPromotion(
                        @Valid @RequestBody CreatePromotionRequest request) {
                PromotionResponse promotion = promotionService.createPromotion(request, getCurrentOwnerId());
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(APIResponse.<PromotionResponse>builder()
                                                .code(1000)
                                                .message("Tạo khuyến mãi thành công")
                                                .result(promotion)
                                                .build());
        }

        @PatchMapping("/promotions/{id}/toggle")
        @Operation(summary = "Bật/Tắt khuyến mãi", description = "Toggle trạng thái active của khuyến mãi")
        public ResponseEntity<APIResponse<PromotionResponse>> togglePromotionStatus(
                        @Parameter(description = "ID của khuyến mãi") @PathVariable Integer id) {
                PromotionResponse promotion = promotionService.togglePromotionStatus(id, getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<PromotionResponse>builder()
                                .code(1000)
                                .message("Cập nhật trạng thái khuyến mãi thành công")
                                .result(promotion)
                                .build());
        }

        @DeleteMapping("/promotions/{id}")
        @Operation(summary = "Xóa khuyến mãi", description = "Xóa một khuyến mãi")
        public ResponseEntity<APIResponse<Void>> deletePromotion(
                        @Parameter(description = "ID của khuyến mãi") @PathVariable Integer id) {
                promotionService.deletePromotion(id, getCurrentOwnerId());
                return ResponseEntity.ok(APIResponse.<Void>builder()
                                .code(1000)
                                .message("Xóa khuyến mãi thành công")
                                .build());
        }

        // ==================== STATISTICS APIS ====================

    @GetMapping("/statistics/revenue")
    @Operation(summary = "Xem doanh thu", description = "Thống kê doanh thu theo tháng/năm")
    public ResponseEntity<APIResponse<RevenueStatisticsResponse>> getRevenueStatistics(
            @Parameter(description = "Tháng (1-12)") @RequestParam int month,
            @Parameter(description = "Năm") @RequestParam int year) {
        RevenueStatisticsResponse stats = bookingService.getRevenueStatistics(getCurrentOwnerId(), month, year);
        return ResponseEntity.ok(APIResponse.<RevenueStatisticsResponse>builder()
                .code(1000)
                .message("Lấy thống kê doanh thu thành công")
                .result(stats)
                .build());
    }

    @GetMapping("/statistics/revenue-by-location")
    @Operation(summary = "Xem doanh thu theo cơ sở", description = "Thống kê doanh thu từng cơ sở theo ngày/tháng/năm")
    public ResponseEntity<APIResponse<List<LocationRevenueResponse>>> getRevenueByLocation(
            @Parameter(description = "Lọc theo cơ sở (null để lấy tất cả)") @RequestParam(required = false) Integer locationId,
            @Parameter(description = "Tháng (1-12), null nếu lọc theo năm hoặc ngày") @RequestParam(required = false) Integer month,
            @Parameter(description = "Năm, bắt buộc nếu có tháng hoặc để lọc theo năm") @RequestParam(required = false) Integer year,
            @Parameter(description = "Ngày cụ thể (yyyy-MM-dd), null nếu lọc theo tháng/năm") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<LocationRevenueResponse> stats = bookingService.getRevenueByLocation(getCurrentOwnerId(), locationId, month, year, date);
        return ResponseEntity.ok(APIResponse.<List<LocationRevenueResponse>>builder()
                .code(1000)
                .message("Lấy thống kê doanh thu theo cơ sở thành công")
                .result(stats)
                .build());
    }
}
