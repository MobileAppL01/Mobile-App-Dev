package Bookington2.demo.controller;

import Bookington2.demo.dto.BookingResponseDTO;
import Bookington2.demo.dto.TimeSlotDTO;
import Bookington2.demo.dto.request.CreateBookingRequestDTO;
import Bookington2.demo.service.PublicBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Hidden
@Tag(name = "Public Booking API", description = "API endpoints for court booking with time slots")
public class PublicBookingController {

    @Autowired
    private PublicBookingService publicBookingService;

    @GetMapping("/available-slots")
    @Operation(summary = "Get available time slots", description = "Get available time slots for a court on a specific date")
    public ResponseEntity<List<TimeSlotDTO>> getAvailableTimeSlots(
            @Parameter(description = "Court ID") 
            @RequestParam Integer courtId,
            
            @Parameter(description = "Date for booking (YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        
        List<TimeSlotDTO> availableSlots = publicBookingService.getAvailableTimeSlots(courtId, date);
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Create booking", description = "Create a new booking for a court with specific time slots")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody CreateBookingRequestDTO bookingRequest) {
        
        BookingResponseDTO booking = publicBookingService.createBooking(bookingRequest);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Get booking details", description = "Get details of a specific booking")
    public ResponseEntity<BookingResponseDTO> getBookingDetails(
            @Parameter(description = "Booking ID") 
            @PathVariable Integer bookingId) {
        
        BookingResponseDTO booking = publicBookingService.getBookingDetails(bookingId);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Get my bookings", description = "Get current user's booking history")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(
            @Parameter(description = "Status filter (PENDING, CONFIRMED, COMPLETED, CANCELLED)") 
            @RequestParam(required = false) String status,
            
            @Parameter(description = "Page number") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "10") int size) {
        
        List<BookingResponseDTO> bookings = publicBookingService.getMyBookings(status, page, size);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{bookingId}/cancel")
    @PreAuthorize("hasRole('PLAYER')")
    @Operation(summary = "Cancel booking", description = "Cancel a specific booking")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @Parameter(description = "Booking ID") 
            @PathVariable Integer bookingId) {
        
        BookingResponseDTO booking = publicBookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/check-availability")
    @Operation(summary = "Check time slot availability", description = "Check if specific time slots are available for booking")
    public ResponseEntity<Boolean> checkTimeSlotAvailability(
            @Parameter(description = "Court ID") 
            @RequestParam Integer courtId,
            
            @Parameter(description = "Date for booking (YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            
            @Parameter(description = "Start time (hour)") 
            @RequestParam Integer startTime,
            
            @Parameter(description = "End time (hour)") 
            @RequestParam Integer endTime) {
        
        Boolean isAvailable = publicBookingService.checkTimeSlotAvailability(courtId, date, startTime, endTime);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/payment-methods")
    @Operation(summary = "Get available payment methods", description = "Get list of available payment methods")
    public ResponseEntity<List<String>> getAvailablePaymentMethods() {
        List<String> paymentMethods = publicBookingService.getAvailablePaymentMethods();
        return ResponseEntity.ok(paymentMethods);
    }
}
