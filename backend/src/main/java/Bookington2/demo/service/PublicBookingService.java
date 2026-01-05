package Bookington2.demo.service;

import Bookington2.demo.dto.BookingResponseDTO;
import Bookington2.demo.dto.TimeSlotDTO;
import Bookington2.demo.dto.request.CreateBookingRequestDTO;
import Bookington2.demo.entity.Booking;
import Bookington2.demo.entity.Court;
import Bookington2.demo.entity.Location;
import Bookington2.demo.entity.User;
import Bookington2.demo.enums.BookingStatus;
import Bookington2.demo.enums.PaymentMethod;
import Bookington2.demo.repository.BookingRepository;
import Bookington2.demo.repository.CourtRepository;
import Bookington2.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TimeSlotDTO> getAvailableTimeSlots(Integer courtId, LocalDate date) {
        Court court = courtRepository.findByIdAndDeletedFalse(courtId)
            .orElseThrow(() -> new RuntimeException("Court not found with id: " + courtId));
        
        Location location = court.getLocation();
        
        // Get all booked hours for the court on the specific date
        List<Integer> bookedHours = bookingRepository.findBookedHoursByCourtAndDate(courtId, date);
        
        List<TimeSlotDTO> availableSlots = new ArrayList<>();
        
        // Generate time slots from open time to close time
        for (int hour = location.getOpenTime().getHour(); hour < location.getCloseTime().getHour(); hour++) {
            TimeSlotDTO slot = new TimeSlotDTO();
            slot.setStartHour(hour);
            slot.setEndHour(hour + 1);
            slot.setStartTimeDisplay(formatTime(hour));
            slot.setEndTimeDisplay(formatTime(hour + 1));
            slot.setPrice(calculatePriceForHour(hour, location.getPricePerHour()));
            slot.setAvailable(!bookedHours.contains(hour));
            slot.setStatus(bookedHours.contains(hour) ? "BOOKED" : "AVAILABLE");
            availableSlots.add(slot);
        }
        
        return availableSlots;
    }

    @Transactional
    public BookingResponseDTO createBooking(CreateBookingRequestDTO request) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        // Validate court exists
        Court court = courtRepository.findByIdAndDeletedFalse(request.getCourtId())
            .orElseThrow(() -> new RuntimeException("Court not found with id: " + request.getCourtId()));

        // Validate time slot availability
        if (!checkTimeSlotAvailability(request.getCourtId(), request.getBookingDate(), 
                                       request.getStartTimeSlot(), request.getEndTimeSlot())) {
            throw new RuntimeException("Selected time slot is not available");
        }

        // Create booking
        Booking booking = Booking.builder()
            .court(court)
            .player(currentUser)
            .bookingDate(request.getBookingDate())
            .startTimeSlot(request.getStartTimeSlot())
            .endTimeSlot(request.getEndTimeSlot())
            .totalPrice(request.getTotalPrice())
            .paymentMethod(request.getPaymentMethod().toString())
            .status(BookingStatus.PENDING)
            .build();

        Booking savedBooking = bookingRepository.save(booking);
        
        return toBookingResponseDTO(savedBooking);
    }

    public BookingResponseDTO getBookingDetails(Integer bookingId) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        Booking booking = bookingRepository.findByIdAndPlayer_Id(bookingId, currentUser.getId())
            .orElseThrow(() -> new RuntimeException("Booking not found or access denied"));

        return toBookingResponseDTO(booking);
    }

    public List<BookingResponseDTO> getMyBookings(String status, int page, int size) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        List<Booking> bookings;
        if (status != null && !status.isEmpty()) {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            bookings = bookingRepository.findAllByPlayerIdWithFilters(
                currentUser.getId(), bookingStatus, null, null);
        } else {
            bookings = bookingRepository.findAllByPlayerId(currentUser.getId());
        }

        return bookings.stream()
            .map(this::toBookingResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Integer bookingId) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        Booking booking = bookingRepository.findByIdAndPlayer_Id(bookingId, currentUser.getId())
            .orElseThrow(() -> new RuntimeException("Booking not found or access denied"));

        if (booking.getStatus() == BookingStatus.CANCELED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELED);
        Booking savedBooking = bookingRepository.save(booking);

        return toBookingResponseDTO(savedBooking);
    }

    public boolean checkTimeSlotAvailability(Integer courtId, LocalDate date, Integer startTime, Integer endTime) {
        // Check if the requested time slot is available
        List<Integer> bookedHours = bookingRepository.findBookedHoursByCourtAndDate(courtId, date);
        
        // Check if any hour in the requested range is already booked
        for (int hour = startTime; hour < endTime; hour++) {
            if (bookedHours.contains(hour)) {
                return false;
            }
        }
        
        return true;
    }

    public List<String> getAvailablePaymentMethods() {
        return java.util.Arrays.stream(PaymentMethod.values())
            .map(PaymentMethod::getDisplayName)
            .collect(Collectors.toList());
    }

    private BookingResponseDTO toBookingResponseDTO(Booking booking) {
        Court court = booking.getCourt();
        Location location = court.getLocation();
        
        // Get payment method display name
        String paymentMethodDisplay = booking.getPaymentMethod();
        try {
            PaymentMethod pm = PaymentMethod.valueOf(booking.getPaymentMethod());
            paymentMethodDisplay = pm.getDisplayName();
        } catch (IllegalArgumentException e) {
            // Keep original value if enum not found
        }
        
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
            .paymentMethodDisplay(paymentMethodDisplay)
            .createdAt(booking.getCreatedAt())
            .build();
    }

    private String formatTime(int hour) {
        return String.format("%02d:00", hour);
    }

    private Float calculatePriceForHour(int hour, Integer basePrice) {
        // Dynamic pricing based on time
        if (hour >= 17 && hour <= 20) { // Peak hours
            return basePrice * 1.2f;
        } else if (hour >= 6 && hour <= 9) { // Morning hours
            return basePrice * 0.9f;
        }
        return basePrice.floatValue();
    }
}
