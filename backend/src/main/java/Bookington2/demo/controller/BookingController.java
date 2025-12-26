package Bookington2.demo.controller;

import Bookington2.demo.dto.BookingDTO;
import Bookington2.demo.dto.TimeSlotDTO;
import Bookington2.demo.dto.TimeSlotView;
import Bookington2.demo.entity.Booking;
import Bookington2.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    BookingService bookingService;
    @GetMapping(path = "/bookingHistory")
    public List<BookingDTO> getBookingHistory(@RequestParam String user) {
            return bookingService.getBookingHistory(user);
    }



    @GetMapping(path = "/available")
    public List<Integer> getAvailableTimeSlotOfCourtAtDate(@RequestParam LocalDate date, @RequestParam Integer court) {
        return bookingService.getAvailableTimeSlotsOfCourtAtDate(court,date);
    }

}
