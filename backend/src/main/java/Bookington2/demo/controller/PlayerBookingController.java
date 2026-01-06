package Bookington2.demo.controller;

import Bookington2.demo.dto.notification.BriefNotificationResponse;
import Bookington2.demo.dto.notification.NotificationDetailResponse;
import Bookington2.demo.dto.player.*;
import Bookington2.demo.dto.request.APIResponse;
import Bookington2.demo.enums.BookingStatus;
import Bookington2.demo.service.NotificationService;
import Bookington2.demo.service.PlayerBookingService;
import Bookington2.demo.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@PreAuthorize("hasAnyRole('PLAYER')")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Player Booking API", description = "API đặt sân cho người chơi")
public class PlayerBookingController {

        private final PlayerBookingService bookingService;
        @Autowired
        private NotificationService notificationService;

        // Tạm thời hard-code playerId, sau này sẽ lấy từ JWT Token
        private Integer getCurrentPlayerId() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                return ((UserDetailsImpl) auth.getPrincipal()).getId();
        }

        // ==================== AVAILABILITY API ====================

        @GetMapping("/courts/{courtId}/availability")
        @Operation(summary = "Kiểm tra tình trạng sân trống", description = "Trả về danh sách các khung giờ đã bị đặt và còn trống trong một ngày cụ thể")
        public ResponseEntity<APIResponse<CourtAvailabilityResponse>> getCourtAvailability(
                        @Parameter(description = "ID của sân") @PathVariable Integer courtId,
                        @Parameter(description = "Ngày cần kiểm tra (yyyy-MM-dd)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

                CourtAvailabilityResponse availability = bookingService.getCourtAvailability(courtId, date);

                return ResponseEntity.ok(APIResponse.<CourtAvailabilityResponse>builder()
                                .code(1000)
                                .message("Lấy thông tin sân trống thành công")
                                .result(availability)
                                .build());
        }

        // ==================== BOOKING APIs ====================

        @PostMapping("/bookings")
        @Operation(summary = "Tạo yêu cầu đặt sân", description = "Người dùng gửi yêu cầu đặt sân. Hệ thống tính toán giá và lưu trạng thái chờ.")
        public ResponseEntity<APIResponse<BookingDetailResponse>> createBooking(
                        @Valid @RequestBody CreateBookingRequest request) {

                BookingDetailResponse booking = bookingService.createBooking(request, getCurrentPlayerId());

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(APIResponse.<BookingDetailResponse>builder()
                                                .code(1000)
                                                .message("Đặt sân thành công, vui lòng thanh toán")
                                                .result(booking)
                                                .build());
        }

        @GetMapping("/bookings/my-history")
        @Operation(summary = "Xem lịch sử đặt sân của tôi", description = "Lấy danh sách booking của user đang đăng nhập")
        public ResponseEntity<APIResponse<List<MyBookingResponse>>> getMyBookings(
                        @Parameter(description = "Lọc theo trạng thái") @RequestParam(required = false) BookingStatus status,

                        @Parameter(description = "Từ ngày (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,

                        @Parameter(description = "Đến ngày (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

                List<MyBookingResponse> bookings = bookingService.getMyBookings(
                                getCurrentPlayerId(), status, fromDate, toDate);

                return ResponseEntity.ok(APIResponse.<List<MyBookingResponse>>builder()
                                .code(1000)
                                .message("Lấy lịch sử đặt sân thành công")
                                .result(bookings)
                                .build());
        }

        @GetMapping("/bookings/{id}")
        @Operation(summary = "Xem chi tiết booking", description = "Lấy thông tin chi tiết của một booking")
        public ResponseEntity<APIResponse<BookingDetailResponse>> getBookingDetail(
                        @Parameter(description = "ID của booking") @PathVariable Integer id) {

                BookingDetailResponse booking = bookingService.getBookingDetail(id, getCurrentPlayerId());

                return ResponseEntity.ok(APIResponse.<BookingDetailResponse>builder()
                                .code(1000)
                                .message("Lấy thông tin booking thành công")
                                .result(booking)
                                .build());
        }

        @PutMapping("/bookings/{id}/cancel")
        @Operation(summary = "Hủy đặt sân", description = "Người dùng tự hủy lịch đặt. Chỉ được hủy khi trạng thái là PENDING hoặc CONFIRMED")
        public ResponseEntity<APIResponse<CancelBookingResponse>> cancelBooking(
                        @Parameter(description = "ID của booking") @PathVariable Integer id) {

                CancelBookingResponse response = bookingService.cancelBooking(id, getCurrentPlayerId());

                return ResponseEntity.ok(APIResponse.<CancelBookingResponse>builder()
                                .code(1000)
                                .message(response.getMessage())
                                .result(response)
                                .build());
        }

        @GetMapping("/notifications")
        @Operation(summary = "Lấy danh sách thông báo", description = "Danh sách thông báo tóm gọn có trường checked để biết người dùng đã đọc hay chưa")
        public ResponseEntity<List<BriefNotificationResponse>> getBriefNotifications() {
                return notificationService.getListNotification(getCurrentPlayerId());
        }

        @GetMapping("/notifications/{notification_id}")
        @Operation(summary = "Lấy chi tiết thông báo", description = "Thông tin chi tiết của một thông báo")
        public ResponseEntity<NotificationDetailResponse> getBriefNotifications(@PathVariable Integer notification_id) {
                return notificationService.getNotification(notification_id, getCurrentPlayerId());
        }

        @DeleteMapping("/notifications/{id}")
        @Operation(summary = "Xóa thông báo", description = "Xóa một thông báo theo ID")
        public ResponseEntity<APIResponse<Void>> deleteNotification(@PathVariable Integer id) {
                notificationService.deleteNotification(id, getCurrentPlayerId());
                return ResponseEntity.ok(APIResponse.<Void>builder()
                                .code(1000)
                                .message("Xóa thông báo thành công")
                                .build());
        }

        @DeleteMapping("/notifications")
        @Operation(summary = "Xóa tất cả thông báo", description = "Xóa toàn bộ thông báo của người chơi")
        public ResponseEntity<APIResponse<Void>> deleteAllNotifications() {
                notificationService.deleteAllNotifications(getCurrentPlayerId());
                return ResponseEntity.ok(APIResponse.<Void>builder()
                                .code(1000)
                                .message("Đã xóa tất cả thông báo")
                                .build());
        }
}
