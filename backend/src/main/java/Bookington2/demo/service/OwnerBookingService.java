package Bookington2.demo.service;

import Bookington2.demo.dto.owner.BookingResponse;
import Bookington2.demo.dto.owner.RevenueStatisticsResponse;
import Bookington2.demo.entity.Booking;
import Bookington2.demo.enums.BookingStatus;
import Bookington2.demo.exception.AppException;
import Bookington2.demo.exception.ErrorCode;
import Bookington2.demo.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerBookingService {

    private final BookingRepository bookingRepository;

    public List<BookingResponse> getBookings(Integer ownerId, Integer locationId, LocalDate date, BookingStatus status) {
        return bookingRepository.findAllByOwnerWithFilters(ownerId, locationId, date, status)
                .stream()
                .map(this::toBookingResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(Integer bookingId, Integer ownerId) {
        Booking booking = bookingRepository.findByIdAndOwnerId(bookingId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));
        return toBookingResponse(booking);
    }

    @Transactional
    public BookingResponse updateBookingStatus(Integer bookingId, BookingStatus status, Integer ownerId) {
        Booking booking = bookingRepository.findByIdAndOwnerId(bookingId, ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        // Validate status transition
        validateStatusTransition(booking.getStatus(), status);

        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return toBookingResponse(booking);
    }

    public RevenueStatisticsResponse getRevenueStatistics(Integer ownerId, int month, int year) {
        Long totalBookings = bookingRepository.countByOwnerIdAndMonth(ownerId, month, year);
        Long canceledBookings = bookingRepository.countByOwnerIdAndStatusAndMonth(ownerId, BookingStatus.CANCELED, month, year);
        Long completedBookings = bookingRepository.countByOwnerIdAndStatusAndMonth(ownerId, BookingStatus.COMPLETED, month, year);
        Long pendingBookings = bookingRepository.countByOwnerIdAndStatusAndMonth(ownerId, BookingStatus.PENDING, month, year);
        Long totalRevenue = bookingRepository.sumRevenueByOwnerIdAndMonth(ownerId, month, year);

        return RevenueStatisticsResponse.builder()
                .totalRevenue(totalRevenue != null ? totalRevenue : 0L)
                .totalBookings(totalBookings != null ? totalBookings : 0L)
                .canceledBookings(canceledBookings != null ? canceledBookings : 0L)
                .completedBookings(completedBookings != null ? completedBookings : 0L)
                .pendingBookings(pendingBookings != null ? pendingBookings : 0L)
                .build();
    }

    private void validateStatusTransition(BookingStatus currentStatus, BookingStatus newStatus) {
        // Define valid transitions
        switch (currentStatus) {
            case PENDING:
                if (newStatus != BookingStatus.CONFIRMED && newStatus != BookingStatus.CANCELED) {
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
                }
                break;
            case CONFIRMED:
                if (newStatus != BookingStatus.COMPLETED && newStatus != BookingStatus.CANCELED) {
                    throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
                }
                break;
            case COMPLETED:
            case CANCELED:
                throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
        }
    }

    private BookingResponse toBookingResponse(Booking booking) {
        String playerName = booking.getPlayer() != null
                ? booking.getPlayer().getFirstName() + " " + booking.getPlayer().getLastName()
                : "N/A";
        String playerPhone = booking.getPlayer() != null ? booking.getPlayer().getPhone() : "N/A";

        return BookingResponse.builder()
                .id(booking.getId())
                .courtName(booking.getCourt().getName())
                .locationName(booking.getCourt().getLocation().getName())
                .playerName(playerName)
                .playerPhone(playerPhone)
                .startHours(booking.getStartHours())
                .startTimeSlot(booking.getStartTimeSlot())
                .endTimeSlot(booking.getEndTimeSlot())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .bookingDate(booking.getBookingDate())
                .paymentMethod(booking.getPaymentMethod())
                .build();
    }
}
