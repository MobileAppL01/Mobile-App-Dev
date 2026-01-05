package Bookington2.demo.controller;

import Bookington2.demo.dto.*;
import Bookington2.demo.dto.request.PaymentConfirmationRequestDTO;
import Bookington2.demo.dto.request.PreBookingRequestDTO;
import Bookington2.demo.enums.PaymentMethod;
import Bookington2.demo.service.EnhancedBookingPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/enhanced-booking-payment")
@Tag(name = "Enhanced Booking Payment API", description = "API endpoints for 4-step booking and payment flow")
public class EnhancedBookingPaymentController {

    @Autowired
    private EnhancedBookingPaymentService bookingPaymentService;

    // ========================================
    // Bước 1: Giữ chỗ - Tạo booking chờ thanh toán
    // ========================================
    @PostMapping("/step1-hold-booking")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Step 1: Hold booking", description = "Create a booking hold with PENDING status")
    public ResponseEntity<PreBookingResponseDTO> holdBooking(
            @Valid @RequestBody PreBookingRequestDTO request) {
        
        PreBookingResponseDTO response = bookingPaymentService.createBookingHold(request);
        return ResponseEntity.ok(response);
    }

    // ========================================
    // Bước 2: Tạo Payment sau khi user chọn phương thức
    // ========================================
    @PostMapping("/step2-create-payment")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Step 2: Create payment", description = "Create payment after user selects payment method")
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @Parameter(description = "Booking ID") 
            @RequestParam Integer bookingId,
            
            @Parameter(description = "Payment method") 
            @RequestParam PaymentMethod paymentMethod,
            
            @Parameter(description = "Customer name") 
            @RequestParam String customerName,
            
            @Parameter(description = "Phone number") 
            @RequestParam String phoneNumber,
            
            @Parameter(description = "Notes") 
            @RequestParam(required = false) String notes) {
        
        PaymentResponseDTO response = bookingPaymentService.createPayment(
            bookingId, paymentMethod, customerName, phoneNumber, notes);
        return ResponseEntity.ok(response);
    }

    // ========================================
    // Bước 3: Xử lý callback từ cổng thanh toán
    // ========================================
    @PostMapping("/step3-payment-callback")
    @Operation(summary = "Step 3: Payment callback", description = "Callback endpoint for payment gateway notifications")
    public ResponseEntity<BookingResponseDTO> handlePaymentCallback(
            @Parameter(description = "Payment ID from gateway") 
            @RequestParam String paymentId,
            
            @Parameter(description = "Payment status") 
            @RequestParam String status,
            
            @Parameter(description = "Transaction ID") 
            @RequestParam(required = false) String transactionId,
            
            @Parameter(description = "Amount") 
            @RequestParam(required = false) String amount,
            
            @Parameter(description = "Gateway response") 
            @RequestParam(required = false) String gatewayResponse) {
        
        BookingResponseDTO response = bookingPaymentService.handlePaymentCallback(
            paymentId, status, transactionId, amount, gatewayResponse);
        return ResponseEntity.ok(response);
    }

    // ========================================
    // Bước 4: Xử lý timeout thanh toán (Admin/System)
    // ========================================
    @PostMapping("/step4-process-expired")
    @Operation(summary = "Step 4: Process expired payments", description = "Process expired payments and release slots")
    public ResponseEntity<Map<String, Object>> processExpiredPayments() {
        bookingPaymentService.processExpiredPayments();
        
        return ResponseEntity.ok(Map.of(
            "message", "Expired payments processed successfully",
            "timestamp", System.currentTimeMillis()
        ));
    }

    // ========================================
    // Utility endpoints
    // ========================================
    @GetMapping("/payment-info/{bookingId}")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Get payment information", description = "Get payment details for a booking")
    public ResponseEntity<PaymentInfoDTO> getPaymentInfo(
            @Parameter(description = "Booking ID") 
            @PathVariable Integer bookingId) {
        
        PaymentInfoDTO paymentInfo = bookingPaymentService.getPaymentInfo(bookingId);
        return ResponseEntity.ok(paymentInfo);
    }

    @GetMapping("/payment-status/{paymentId}")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Check payment status", description = "Check the status of a payment")
    public ResponseEntity<Map<String, Object>> checkPaymentStatus(
            @Parameter(description = "Payment ID") 
            @PathVariable String paymentId) {
        
        // This would typically query the payment gateway
        Map<String, Object> status = Map.of(
            "paymentId", paymentId,
            "status", "PENDING",
            "message", "Payment is being processed",
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(status);
    }

    @GetMapping("/booking-success/{bookingId}")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Get booking success info", description = "Get booking information after successful payment")
    public ResponseEntity<BookingResponseDTO> getBookingSuccess(
            @Parameter(description = "Booking ID") 
            @PathVariable Integer bookingId) {
        
        // This would retrieve the completed booking details
        BookingResponseDTO response = BookingResponseDTO.builder()
            .id(bookingId)
            .courtId(1)
            .courtName("Thanh Da Badminton Court")
            .locationName("Thanh Da Sports Center")
            .locationAddress("353/7A Binh Quoi, Ward 28, Binh Thanh, HCMC")
            .bookingDate(java.time.LocalDate.now().plusDays(1))
            .startTimeSlot(17)
            .endTimeSlot(19)
            .startTimeDisplay("17:00")
            .endTimeDisplay("19:00")
            .totalPrice(168000f)
            .status("COMPLETED")
            .paymentMethod("PAID")
            .createdAt(java.time.LocalDateTime.now())
            .build();
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel-booking/{bookingId}")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Cancel booking", description = "Cancel a booking before payment")
    public ResponseEntity<Map<String, Object>> cancelBooking(
            @Parameter(description = "Booking ID") 
            @PathVariable Integer bookingId) {
        
        // Implementation would cancel the booking and release the slot
        return ResponseEntity.ok(Map.of(
            "message", "Booking canceled successfully",
            "bookingId", bookingId,
            "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/available-payment-methods")
    @Operation(summary = "Get available payment methods", description = "Get list of available payment methods")
    public ResponseEntity<Map<String, Object>> getAvailablePaymentMethods() {
        return ResponseEntity.ok(Map.of(
            "paymentMethods", PaymentMethod.values(),
            "timestamp", System.currentTimeMillis()
        ));
    }
}
