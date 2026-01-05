package Bookington2.demo.service;

import Bookington2.demo.dto.*;
import Bookington2.demo.dto.request.PaymentConfirmationRequestDTO;
import Bookington2.demo.dto.request.PreBookingRequestDTO;
import Bookington2.demo.entity.Booking;
import Bookington2.demo.entity.Court;
import Bookington2.demo.entity.Location;
import Bookington2.demo.entity.Payment;
import Bookington2.demo.entity.User;
import Bookington2.demo.enums.BookingStatus;
import Bookington2.demo.enums.PaymentMethod;
import Bookington2.demo.enums.PaymentStatus;
import Bookington2.demo.repository.BookingRepository;
import Bookington2.demo.repository.CourtRepository;
import Bookington2.demo.repository.PaymentRepository;
import Bookington2.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class EnhancedBookingPaymentService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SePayService sePayService;

    @Value("${app.bank.account:}")
    private String bankAccount;

    @Value("${app.bank.name:}")
    private String bankName;

    @Value("${app.account.name:}")
    private String accountName;

    // ========================================
    // Bước 1: Giữ chỗ - Tạo booking chờ thanh toán
    // ========================================
    @Transactional
    public PreBookingResponseDTO createBookingHold(PreBookingRequestDTO request) {
        // Validate court exists
        Court court = courtRepository.findByIdAndDeletedFalse(request.getCourtId())
            .orElseThrow(() -> new RuntimeException("Court not found with id: " + request.getCourtId()));

        Location location = court.getLocation();

        // Check if time slot is available
        if (!isTimeSlotAvailable(request.getCourtId(), request.getBookingDate(), 
                                request.getStartTimeSlot(), request.getEndTimeSlot())) {
            return PreBookingResponseDTO.builder()
                .available(false)
                .status("NOT_AVAILABLE")
                .build();
        }

        // Calculate total hours and price
        Integer totalHours = request.getEndTimeSlot() - request.getStartTimeSlot();
        Float totalPrice = calculateTotalPrice(location.getPricePerHour(), totalHours, request.getStartTimeSlot());

        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        // Create booking with PENDING status
        Booking booking = Booking.builder()
            .court(court)
            .player(currentUser)
            .bookingDate(request.getBookingDate())
            .startTimeSlot(request.getStartTimeSlot())
            .endTimeSlot(request.getEndTimeSlot())
            .totalPrice(totalPrice)
            .status(BookingStatus.PENDING)
            .paymentMethod("PENDING")
            .build();

        Booking savedBooking = bookingRepository.save(booking);

        return PreBookingResponseDTO.builder()
            .bookingId(savedBooking.getId().toString())
            .courtId(court.getId())
            .courtName(court.getName())
            .courtAddress(location.getAddress())
            .bookingDate(request.getBookingDate())
            .startTimeSlot(request.getStartTimeSlot())
            .endTimeSlot(request.getEndTimeSlot())
            .startTimeDisplay(formatTime(request.getStartTimeSlot()))
            .endTimeDisplay(formatTime(request.getEndTimeSlot()))
            .totalHours(totalHours)
                .pricePerHour(location.getPricePerHour() == null ? null : location.getPricePerHour().floatValue())
                .totalPrice(totalPrice)
            .available(true)
            .status("PENDING_PAYMENT")
            .courtOpenTime(location.getOpenTime())
            .courtCloseTime(location.getCloseTime())
            .build();
    }

    // ========================================
    // Bước 2: Tạo Payment sau khi user chọn phương thức
    // ========================================
    @Transactional
    public PaymentResponseDTO createPayment(Integer bookingId, PaymentMethod paymentMethod, 
                                          String customerName, String phoneNumber, String notes) {
        
        // Find booking
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        // Validate booking status
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in PENDING status");
        }

        // Generate payment details
        String paymentId = "PAY" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String orderReference = "ORDER" + booking.getId() + System.currentTimeMillis();
        
        // Generate payment content
        String paymentContent = String.format("DAT SAN %s %s %s-%s", 
            booking.getCourt().getName(), 
            booking.getBookingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            formatTime(booking.getStartTimeSlot()),
            formatTime(booking.getEndTimeSlot()));

        // Create payment record
        Payment payment = Payment.builder()
            .booking(booking)
            .status(PaymentStatus.PENDING)
            .amount(BigDecimal.valueOf(booking.getTotalPrice()))
            .paymentMethod(paymentMethod.toString())
            .paymentId(paymentId)
            .orderReference(orderReference)
            .build();

        // Generate QR code and payment URL based on payment method
        if (paymentMethod == PaymentMethod.BANK_TRANSFER || paymentMethod == PaymentMethod.VNPAY) {
            String qrCodeUrl = sePayService.generateQRCode(
                booking.getTotalPrice().toString(),
                paymentContent,
                orderReference
            );
            
            payment.setQrCodeUrl(qrCodeUrl);
            payment.setPaymentUrl(qrCodeUrl);
        }

        Payment savedPayment = paymentRepository.save(payment);

        // Update booking status to CONFIRMED
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentMethod(paymentMethod.toString());
        bookingRepository.save(booking);

        return PaymentResponseDTO.builder()
            .paymentId(savedPayment.getPaymentId())
            .bookingId(booking.getId().toString())
            .paymentUrl(savedPayment.getPaymentUrl())
            .qrCodeUrl(savedPayment.getQrCodeUrl())
            .status(savedPayment.getStatus().toString())
            .message("Payment created. Please complete payment within 15 minutes.")
            .bankAccount(bankAccount)
            .bankName(bankName)
            .accountName(accountName)
            .amount(savedPayment.getAmount().toString())
            .content(paymentContent)
            .expiryTime(savedPayment.getExpiredAt().toEpochSecond(java.time.ZoneOffset.UTC))
            .build();
    }

    // ========================================
    // Bước 3: Xử lý callback từ cổng thanh toán
    // ========================================
    @Transactional
    public BookingResponseDTO handlePaymentCallback(String paymentId, String status, 
                                                   String transactionId, String amount, 
                                                   String gatewayResponse) {
        
        // Find payment by paymentId
        Payment payment = paymentRepository.findByPaymentId(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

        Booking booking = payment.getBooking();

        // Update payment based on callback status
        switch (status.toUpperCase()) {
            case "SUCCESS":
            case "COMPLETED":
                payment.markAsSuccessful(transactionId, gatewayResponse);
                booking.setStatus(BookingStatus.COMPLETED);
                
                // Send confirmation notification
                sendBookingConfirmation(booking);
                break;
                
            case "FAILED":
                payment.markAsFailed(transactionId, gatewayResponse);
                booking.setStatus(BookingStatus.CANCELED);
                break;
                
            case "PENDING":
                // Keep current status, just update transaction info
                payment.setTransactionId(transactionId);
                payment.setGatewayResponse(gatewayResponse);
                break;
                
            default:
                throw new RuntimeException("Unknown payment status: " + status);
        }

        paymentRepository.save(payment);
        Booking savedBooking = bookingRepository.save(booking);

        return convertToBookingResponseDTO(savedBooking);
    }

    // ========================================
    // Bước 4: Xử lý timeout thanh toán
    // ========================================
    @Transactional
    public void processExpiredPayments() {
        List<Payment> expiredPayments = paymentRepository.findExpiredPayments(
            PaymentStatus.PENDING, LocalDateTime.now()
        );

        for (Payment payment : expiredPayments) {
            payment.markAsExpired();
            Booking booking = payment.getBooking();
            
            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                booking.setStatus(BookingStatus.EXPIRED);
            }
            
            paymentRepository.save(payment);
            bookingRepository.save(booking);
        }
    }

    // ========================================
    // Helper methods
    // ========================================
    public PaymentInfoDTO getPaymentInfo(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        Court court = booking.getCourt();
        Location location = court.getLocation();

        Integer totalHours = booking.getEndTimeSlot() - booking.getStartTimeSlot();

        // Generate payment content
        String paymentContent = String.format("DAT SAN %s %s %s-%s", 
            court.getName(), 
            booking.getBookingDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            formatTime(booking.getStartTimeSlot()),
            formatTime(booking.getEndTimeSlot()));

        // Get existing payment if any
        Payment existingPayment = paymentRepository.findByBookingIdAndStatus(bookingId, PaymentStatus.PENDING)
            .orElse(null);

        return PaymentInfoDTO.builder()
            .courtName(court.getName())
            .courtAddress(location.getAddress())
            .bookingDate(booking.getBookingDate())
            .courtNumber(court.getId())
            .startTimeSlot(booking.getStartTimeSlot())
            .endTimeSlot(booking.getEndTimeSlot())
            .startTimeDisplay(formatTime(booking.getStartTimeSlot()))
            .endTimeDisplay(formatTime(booking.getEndTimeSlot()))
            .totalHours(totalHours)
            .totalPrice(booking.getTotalPrice())
            .bookingId(booking.getId().toString())
            .status(booking.getStatus().toString())
            .availablePaymentMethods(Arrays.asList(PaymentMethod.values()))
            .courtOpenTime(location.getOpenTime())
            .courtCloseTime(location.getCloseTime())
            .qrCodeUrl(existingPayment != null ? existingPayment.getQrCodeUrl() : null)
            .paymentUrl(existingPayment != null ? existingPayment.getPaymentUrl() : null)
            .bankAccount(bankAccount)
            .bankName(bankName)
            .accountName(accountName)
            .amount(booking.getTotalPrice().toString())
            .content(paymentContent)
            .build();
    }

    private boolean isTimeSlotAvailable(Integer courtId, java.time.LocalDate date, Integer startTime, Integer endTime) {
        List<Integer> bookedHours = bookingRepository.findBookedHoursByCourtAndDate(courtId, date);
        
        for (int hour = startTime; hour < endTime; hour++) {
            if (bookedHours.contains(hour)) {
                return false;
            }
        }
        
        return true;
    }

    private Float calculateTotalPrice(Integer basePrice, Integer totalHours, Integer startHour) {
        float totalPrice = 0;
        for (int hour = startHour; hour < startHour + totalHours; hour++) {
            if (hour >= 17 && hour <= 20) { // Peak hours
                totalPrice += basePrice * 1.2f;
            } else if (hour >= 6 && hour <= 9) { // Morning hours
                totalPrice += basePrice * 0.9f;
            } else {
                totalPrice += basePrice;
            }
        }
        
        return BigDecimal.valueOf(totalPrice).setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    private String formatTime(int hour) {
        return String.format("%02d:00", hour);
    }

    private void sendBookingConfirmation(Booking booking) {
        // Implement email/SMS confirmation
        System.out.println("Booking confirmation sent for: " + booking.getId());
    }

    private BookingResponseDTO convertToBookingResponseDTO(Booking booking) {
        Court court = booking.getCourt();
        Location location = court.getLocation();
        
        return BookingResponseDTO.builder()
            .id(booking.getId())
            .courtId(court.getId())
            .courtName(court.getName())
            .locationName(location.getName())
            .locationAddress(location.getAddress())
            .bookingDate(booking.getBookingDate())
            .startTimeSlot(booking.getStartTimeSlot())
            .endTimeSlot(booking.getEndTimeSlot())
            .startTimeDisplay(formatTime(booking.getStartTimeSlot()))
            .endTimeDisplay(formatTime(booking.getEndTimeSlot()))
            .totalPrice(booking.getTotalPrice())
            .status(booking.getStatus().toString())
            .paymentMethod(booking.getPaymentMethod())
            .createdAt(booking.getCreatedAt())
            .build();
    }
}
