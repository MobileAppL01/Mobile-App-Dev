package Bookington2.demo.service;

import Bookington2.demo.dto.BookingResponseDTO;
import Bookington2.demo.dto.CourtSearchDTO;
import Bookington2.demo.dto.TimeSlotDTO;
import Bookington2.demo.dto.request.CreateBookingRequestDTO;
import Bookington2.demo.entity.Booking;
import Bookington2.demo.entity.Court;
import Bookington2.demo.entity.Location;
import Bookington2.demo.enums.BookingStatus;
import Bookington2.demo.repository.BookingRepository;
import Bookington2.demo.repository.CourtRepository;
import Bookington2.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicService {

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    public List<CourtSearchDTO> searchCourts(String keyword, String province, String district, 
                                           Integer minPrice, Integer maxPrice, int page, int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size);
        
        List<Location> locations = locationRepository.findLocationsWithFilters(
            province, district, minPrice, maxPrice, pageRequest);
        
        List<Court> courts = new ArrayList<>();
        for (Location location : locations) {
            List<Court> locationCourts = courtRepository.findByLocationAndDeletedFalse(location);
            if (keyword != null && !keyword.trim().isEmpty()) {
                locationCourts = locationCourts.stream()
                    .filter(court -> court.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                   location.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                   location.getAddress().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
            }
            courts.addAll(locationCourts);
        }
        
        return courts.stream()
            .map(this::toCourtSearchDTO)
            .collect(Collectors.toList());
    }

    public CourtSearchDTO getCourtDetails(Integer courtId) {
        Court court = courtRepository.findById(courtId)
            .orElseThrow(() -> new RuntimeException("Court not found with id: " + courtId));
        
        return toCourtSearchDTO(court);
    }

    public List<TimeSlotDTO> getAvailableTimeSlots(Integer courtId, LocalDate date) {
        Court court = courtRepository.findById(courtId)
            .orElseThrow(() -> new RuntimeException("Court not found with id: " + courtId));
        
        Location location = court.getLocation();
        
        List<Integer> bookedHours = bookingRepository.findBookedHoursByCourtAndDate(courtId, date);
        
        List<TimeSlotDTO> availableSlots = new ArrayList<>();
        
        for (int hour = location.getOpenTime().getHour(); hour < location.getCloseTime().getHour(); hour++) {
            if (!bookedHours.contains(hour)) {
                TimeSlotDTO slot = new TimeSlotDTO();
                slot.setStartHour(hour);
                slot.setEndHour(hour + 1);
                slot.setPrice(calculatePriceForHour(hour, location.getPricePerHour()));
                slot.setAvailable(true);
                availableSlots.add(slot);
            }
        }
        
        return availableSlots;
    }

    @Transactional
    public BookingResponseDTO createBooking(CreateBookingRequestDTO request) {
        Court court = courtRepository.findById(request.getCourtId())
            .orElseThrow(() -> new RuntimeException("Court not found with id: " + request.getCourtId()));
        
        // Validate that the requested time slots are available
        List<Integer> bookedHours = bookingRepository.findBookedHoursByCourtAndDate(
            request.getCourtId(), request.getBookingDate());
        
        for (int hour = request.getStartTimeSlot(); hour < request.getEndTimeSlot(); hour++) {
            if (bookedHours.contains(hour)) {
                throw new RuntimeException("Time slot " + hour + ":00-" + (hour + 1) + ":00 is already booked");
            }
        }
        
        // Create booking
        Booking booking = Booking.builder()
            .court(court)
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
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        
        return toBookingResponseDTO(booking);
    }

    @Transactional
    public void cancelBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        
        if (booking.getStatus() == BookingStatus.CANCELED) {
            throw new RuntimeException("Booking is already cancelled");
        }
        
        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    private CourtSearchDTO toCourtSearchDTO(Court court) {
        Location location = court.getLocation();
        return CourtSearchDTO.builder()
            .id(court.getId())
            .name(court.getName())
            .address(location.getAddress())
            .description(location.getDescription())
            .image(location.getImage())
            .rating(location.getRating())
            .pricePerHour(location.getPricePerHour())
            .openTime(location.getOpenTime())
            .closeTime(location.getCloseTime())
            .status(location.getStatus())
            .locationId(location.getId())
            .build();
    }

    private BookingResponseDTO toBookingResponseDTO(Booking booking) {
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

    private Float calculatePriceForHour(int hour, Integer basePrice) {
        // Implement dynamic pricing based on time
        if (hour >= 17 && hour <= 20) { // Peak hours
            return basePrice * 1.2f;
        } else if (hour >= 6 && hour <= 9) { // Morning hours
            return basePrice * 0.9f;
        }
        return basePrice.floatValue();
    }
    
    private String formatTime(int hour) {
        return String.format("%02d:00", hour);
    }
}
