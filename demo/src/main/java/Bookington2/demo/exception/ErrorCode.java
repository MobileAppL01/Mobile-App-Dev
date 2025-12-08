package Bookington2.demo.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATERGORIZED_EXCEPTION(9999, "Uncatergorized exception error"),
    INVALID_MESSAGE_KEY(1001, "Invalid message key"),
    USER_EXITSED(1002, "User has been exitsted"),
    USERNAME_INVALID(1003, "Username must be 3 characters"),
    PASSWORD_INVALID(1004, "Password must be 8 characters"),

    // User errors
    USER_NOT_EXISTED(1005, "User not found"),
    UNAUTHORIZED(1006, "Unauthorized access"),

    // Location errors
    LOCATION_NOT_FOUND(2001, "Location not found"),
    LOCATION_ACCESS_DENIED(2002, "You don't have permission to access this location"),

    // Court errors
    COURT_NOT_FOUND(3001, "Court not found"),
    COURT_ACCESS_DENIED(3002, "You don't have permission to access this court"),
    COURT_NOT_ACTIVE(3003, "Court is not active (under maintenance)"),

    // Booking errors
    BOOKING_NOT_FOUND(4001, "Booking not found"),
    BOOKING_ACCESS_DENIED(4002, "You don't have permission to access this booking"),
    INVALID_STATUS_TRANSITION(4003, "Invalid status transition"),
    BOOKING_DATE_IN_PAST(4004, "Booking date must be in the future"),
    TIME_SLOT_NOT_AVAILABLE(4005, "One or more time slots are already booked"),
    TIME_SLOT_OUTSIDE_OPERATING_HOURS(4006, "Time slot is outside operating hours"),
    BOOKING_CANNOT_CANCEL(4007, "Cannot cancel this booking"),
    TIME_SLOTS_CONFLICT(4008, "Selected time slots conflict with existing bookings"),

    // Promotion errors
    PROMOTION_NOT_FOUND(5001, "Promotion not found"),
    PROMOTION_CODE_EXISTS(5002, "Promotion code already exists"),
    INVALID_DATE_RANGE(5003, "Start date must be before end date"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
