package Bookington2.demo.service;

import Bookington2.demo.dto.player.*;
import Bookington2.demo.entity.Booking;
import Bookington2.demo.entity.Court;
import Bookington2.demo.entity.Location;
import Bookington2.demo.entity.User;
import Bookington2.demo.enums.BookingStatus;
import Bookington2.demo.enums.CourtStatus;
import Bookington2.demo.exception.AppException;
import Bookington2.demo.exception.ErrorCode;
import Bookington2.demo.repository.BookingRepository;
import Bookington2.demo.repository.CourtRepository;
import Bookington2.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerBookingService {

    private final BookingRepository bookingRepository;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;

    /**
     * Check court availability for a specific date
     */
    public CourtAvailabilityResponse getCourtAvailability(Integer courtId, LocalDate date) {
        Court court = courtRepository.findByIdAndDeletedFalse(courtId)
                .orElseThrow(() -> new AppException(ErrorCode.COURT_NOT_FOUND));

        Location location = court.getLocation();
        int openHour = location.getOpenTime().getHour();
        int closeHour = location.getCloseTime().getHour();

        // Get all active bookings for this court on this date
        List<Booking> bookings = bookingRepository.findActiveBookingsByCourtAndDate(courtId, date);

        // Extract all booked hours
        Set<Integer> bookedSlots = new HashSet<>();
        for (Booking booking : bookings) {
            if (booking.getStartHours() != null) {
                bookedSlots.addAll(booking.getStartHours());
            }
            // Backward compatibility with startTimeSlot/endTimeSlot
            if (booking.getStartTimeSlot() != null && booking.getEndTimeSlot() != null) {
                for (int i = booking.getStartTimeSlot(); i < booking.getEndTimeSlot(); i++) {
                    bookedSlots.add(i);
                }
            }
        }

        // Calculate available slots
        List<Integer> availableSlots = IntStream.range(openHour, closeHour)
                .filter(hour -> !bookedSlots.contains(hour))
                .boxed()
                .collect(Collectors.toList());

        return CourtAvailabilityResponse.builder()
                .courtId(courtId)
                .courtName(court.getName())
                .date(date)
                .bookedSlots(new ArrayList<>(bookedSlots))
                .availableSlots(availableSlots)
                .openHour(openHour)
                .closeHour(closeHour)
                .pricePerHour(location.getPricePerHour())
                .build();
    }

    /**
     * Create a new booking
     */
    @Transactional
    public BookingDetailResponse createBooking(CreateBookingRequest request, Integer playerId) {
        // 1. Validate court exists and is active
        Court court = courtRepository.findByIdAndDeletedFalse(request.getCourtId())
                .orElseThrow(() -> new AppException(ErrorCode.COURT_NOT_FOUND));

        if (court.getStatus() != CourtStatus.ACTIVE) {
            throw new AppException(ErrorCode.COURT_NOT_ACTIVE);
        }

        Location location = court.getLocation();

        // 2. Validate booking date is in the future
        if (request.getBookingDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.BOOKING_DATE_IN_PAST);
        }

        // 3. Validate time slots are within operating hours
        int openHour = location.getOpenTime().getHour();
        int closeHour = location.getCloseTime().getHour();
        for (Integer hour : request.getStartHours()) {
            if (hour < openHour || hour >= closeHour) {
                throw new AppException(ErrorCode.TIME_SLOT_OUTSIDE_OPERATING_HOURS);
            }
        }

        // 4. Check for conflicts with existing bookings (with pessimistic locking)
        List<Booking> existingBookings = bookingRepository
                .findActiveBookingsByCourtAndDateWithLock(request.getCourtId(), request.getBookingDate());

        Set<Integer> bookedSlots = new HashSet<>();
        for (Booking booking : existingBookings) {
            if (booking.getStartHours() != null) {
                bookedSlots.addAll(booking.getStartHours());
            }
            if (booking.getStartTimeSlot() != null && booking.getEndTimeSlot() != null) {
                for (int i = booking.getStartTimeSlot(); i < booking.getEndTimeSlot(); i++) {
                    bookedSlots.add(i);
                }
            }
        }

        // Check if any requested slot is already booked
        for (Integer requestedHour : request.getStartHours()) {
            if (bookedSlots.contains(requestedHour)) {
                throw new AppException(ErrorCode.TIME_SLOTS_CONFLICT);
            }
        }

        // 5. Calculate total price
        float totalPrice = location.getPricePerHour() * request.getStartHours().size();

        // 6. Get player
        User player = userRepository.findById(playerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 7. Create booking
        Booking booking = Booking.builder()
                .court(court)
                .player(player)
                .bookingDate(request.getBookingDate())
                .startHours(request.getStartHours())
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .paymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "CASH")
                .build();

        booking = bookingRepository.save(booking);

        log.info("Created booking {} for player {} on court {} date {}",
                booking.getId(), playerId, court.getName(), request.getBookingDate());

        return toBookingDetailResponse(booking);
    }

    /**
     * Get player's booking history
     */
    public List<MyBookingResponse> getMyBookings(Integer playerId, BookingStatus status,
                                                   LocalDate fromDate, LocalDate toDate) {
        List<Booking> bookings = bookingRepository.findAllByPlayerIdWithFilters(
                playerId, status, fromDate, toDate);

        return bookings.stream()
                .map(this::toMyBookingResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get booking detail
     */
    public BookingDetailResponse getBookingDetail(Integer bookingId, Integer playerId) {
        Booking booking = bookingRepository.findByIdAndPlayer_Id(bookingId, playerId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        return toBookingDetailResponse(booking);
    }

    /**
     * Cancel booking
     */
    @Transactional
    public CancelBookingResponse cancelBooking(Integer bookingId, Integer playerId) {
        Booking booking = bookingRepository.findByIdAndPlayer_Id(bookingId, playerId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        // Can only cancel PENDING or CONFIRMED bookings
        if (booking.getStatus() != BookingStatus.PENDING &&
            booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new AppException(ErrorCode.BOOKING_CANNOT_CANCEL);
        }

        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);

        log.info("Booking {} canceled by player {}", bookingId, playerId);

        return CancelBookingResponse.builder()
                .message("Đã hủy lịch đặt sân thành công")
                .bookingId(bookingId)
                .newStatus(BookingStatus.CANCELED)
                .build();
    }

    private BookingDetailResponse toBookingDetailResponse(Booking booking) {
        Location location = booking.getCourt().getLocation();

        return BookingDetailResponse.builder()
                .id(booking.getId())
                .courtId(booking.getCourt().getId())
                .courtName(booking.getCourt().getName())
                .locationId(location.getId())
                .locationName(location.getName())
                .locationAddress(location.getAddress())
                .locationImage(location.getImage())
                .bookingDate(booking.getBookingDate())
                .startHours(booking.getStartHours())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .paymentMethod(booking.getPaymentMethod())
                .createdAt(booking.getCreatedAt())
                .paymentUrl(generatePaymentUrl(booking)) // TODO: Implement actual payment integration
                .build();
    }

    private MyBookingResponse toMyBookingResponse(Booking booking) {
        Location location = booking.getCourt().getLocation();

        return MyBookingResponse.builder()
                .id(booking.getId())
                .courtName(booking.getCourt().getName())
                .locationName(location.getName())
                .locationAddress(location.getAddress())
                .bookingDate(booking.getBookingDate())
                .startHours(booking.getStartHours())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .build();
    }

    private String generatePaymentUrl(Booking booking) {
        // TODO: Implement actual VNPAY integration
        if ("VNPAY".equals(booking.getPaymentMethod())) {
            return "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?booking_id=" + booking.getId();
        }
        return null;
    }
}

