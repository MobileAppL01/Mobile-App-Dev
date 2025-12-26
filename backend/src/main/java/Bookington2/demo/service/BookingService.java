package Bookington2.demo.service;

import Bookington2.demo.dto.BookingDTO;
import Bookington2.demo.dto.OpenTimeDTO;
import Bookington2.demo.dto.TimeSlotDTO;
import Bookington2.demo.dto.TimeSlotView;
import Bookington2.demo.entity.Booking;
import Bookington2.demo.entity.Court;
import Bookington2.demo.repository.BookingRepository;
import Bookington2.demo.repository.CourtRepository;
import Bookington2.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    CourtRepository courtRepository;

    @Autowired
    LocationService locationService;
    private List<TimeSlotView> getBookingOfCourtAtDate(Integer courtId, LocalDate bookingDate) {
        return bookingRepository.findAllByCourtIdAndBookingDate(courtId, bookingDate);
    }
    private BookingDTO toBookingDTO(Booking booking) {
        return new BookingDTO(booking.getCourt().getName(),booking.getCourt().getLocation().getName(),booking.getStartTimeSlot(),booking.getEndTimeSlot(),booking.getStatus(),booking.getBookingDate());
    }
    public List<BookingDTO> getBookingHistory(String userId) {
        return bookingRepository.findAllByPlayer_Id(userId).stream().map(this::toBookingDTO).toList();
    }
    public List<Integer> getAvailableTimeSlotsOfCourtAtDate(int courtId, LocalDate bookingDate) {
        List<TimeSlotView> bookedTimeSlots = getBookingOfCourtAtDate(courtId, bookingDate);
        List<Integer> availableTimeSlots = getAllAvailableTimeSlots(courtId);
        for (TimeSlotView booked : bookedTimeSlots) {
            int start = booked.getStartTimeSlot();
            int end = booked.getEndTimeSlot();
            availableTimeSlots.removeIf(time -> time >= start && time <= end);
        }
        return availableTimeSlots;
    }
    private List<Integer> getAllAvailableTimeSlots(Integer courtId) {
        Court court = courtRepository.findById(courtId).orElse(null);
        if(court == null) {
            throw new RuntimeException("Court not found");
        }
        Integer locationId = court.getLocation().getId();

        OpenTimeDTO openTime = locationRepository.findLocationById(locationId);
        if(openTime == null) {
            throw new RuntimeException("OpenTime not found");
        }
        List<Integer> allTimeSlots = new ArrayList<>();
        for (int i = openTime.getStartHour(); i <= openTime.getEndHour(); i++) {
            allTimeSlots.add(i);
        }
        return allTimeSlots;
    }

}
