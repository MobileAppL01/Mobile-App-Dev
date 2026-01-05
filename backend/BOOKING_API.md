# Court Booking API - Time Slot Management

## Base URL
```
http://localhost:8080/api/bookings
```

## Overview
This API provides comprehensive court booking functionality with time slot selection, availability checking, and booking management.

## Endpoints

### 1. Get Available Time Slots
**GET** `/available-slots`

Get available time slots for a specific court on a given date.

**Parameters:**
- `courtId`: Court ID (required)
- `date`: Date for booking (YYYY-MM-DD format, required)

**Example Request:**
```bash
GET /api/bookings/available-slots?courtId=1&date=2024-01-15
```

**Response:**
```json
[
  {
    "startHour": 6,
    "endHour": 7,
    "startTimeDisplay": "06:00",
    "endTimeDisplay": "07:00",
    "price": 63000,
    "available": true,
    "status": "AVAILABLE"
  },
  {
    "startHour": 17,
    "endHour": 18,
    "startTimeDisplay": "17:00",
    "endTimeDisplay": "18:00",
    "price": 84000,
    "available": false,
    "status": "BOOKED"
  }
]
```

### 2. Create Booking
**POST** `/`

Create a new booking with specific time slots. Requires `PLAYER` role and JWT authentication.

**Request Body:**
```json
{
  "courtId": 1,
  "bookingDate": "2024-01-15",
  "startTimeSlot": 17,
  "endTimeSlot": 19,
  "totalPrice": 168000,
  "paymentMethod": "CASH",
  "notes": "Birthday party booking"
}
```

**Response:**
```json
{
  "id": 123,
  "courtId": 1,
  "courtName": "Thanh Da Badminton Court",
  "locationName": "Thanh Da Sports Center",
  "locationAddress": "353/7A Binh Quoi, Ward 28, Binh Thanh, HCMC",
  "bookingDate": "2024-01-15",
  "startTimeSlot": 17,
  "endTimeSlot": 19,
  "startTimeDisplay": "17:00",
  "endTimeDisplay": "19:00",
  "totalPrice": 168000,
  "status": "PENDING",
  "paymentMethod": "CASH",
  "createdAt": "2024-01-10T10:30:00"
}
```

### 3. Get Booking Details
**GET** `/{bookingId}`

Get details of a specific booking. Requires `PLAYER` role.

**Example Request:**
```bash
GET /api/bookings/123
```

**Response:** Same as Create Booking response

### 4. Get My Bookings
**GET** `/my-bookings`

Get current user's booking history. Requires `PLAYER` role.

**Parameters:**
- `status` (optional): Filter by status (PENDING, CONFIRMED, COMPLETED, CANCELLED)
- `page` (default: 0): Page number
- `size` (default: 10): Page size

**Example Request:**
```bash
GET /api/bookings/my-bookings?status=PENDING&page=0&size=5
```

**Response:**
```json
[
  {
    "id": 123,
    "courtId": 1,
    "courtName": "Thanh Da Badminton Court",
    "bookingDate": "2024-01-15",
    "startTimeSlot": 17,
    "endTimeSlot": 19,
    "startTimeDisplay": "17:00",
    "endTimeDisplay": "19:00",
    "totalPrice": 168000,
    "status": "PENDING"
  }
]
```

### 5. Cancel Booking
**PUT** `/{bookingId}/cancel`

Cancel a specific booking. Requires `PLAYER` role.

**Example Request:**
```bash
PUT /api/bookings/123/cancel
```

**Response:**
```json
{
  "id": 123,
  "status": "CANCELLED",
  "courtName": "Thanh Da Badminton Court",
  "bookingDate": "2024-01-15",
  "startTimeDisplay": "17:00",
  "endTimeDisplay": "19:00"
}
```

### 6. Check Time Slot Availability
**POST** `/check-availability`

Check if specific time slots are available for booking.

**Parameters:**
- `courtId`: Court ID (required)
- `date`: Date for booking (YYYY-MM-DD format, required)
- `startTime`: Start time hour (0-23, required)
- `endTime`: End time hour (1-24, required)

**Example Request:**
```bash
POST /api/bookings/check-availability?courtId=1&date=2024-01-15&startTime=17&endTime=19
```

**Response:**
```json
true
```

## Time Slot Logic

### Time Format
- **24-hour format**: Hours are represented as integers (0-23)
- **Display format**: "HH:00" (e.g., "17:00" for 5 PM)
- **Time slots**: Each slot represents 1 hour (e.g., 17:00-18:00)

### Dynamic Pricing
- **Peak hours** (17:00-20:00): Base price × 1.2
- **Morning hours** (06:00-09:00): Base price × 0.9
- **Regular hours**: Base price

### Availability Rules
1. **Court operating hours**: Only slots within court's open/close times are available
2. **No overlapping**: Cannot book overlapping time slots
3. **Future dates only**: Cannot book past dates
4. **Minimum booking**: At least 1 hour
5. **Maximum booking**: Maximum 8 hours per booking

## Booking Status Values

- `PENDING`: Booking is pending confirmation
- `CONFIRMED`: Booking is confirmed
- `COMPLETED`: Booking has been completed
- `CANCELLED`: Booking has been cancelled

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "End time must be after start time"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Selected time slot is not available"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

## Integration Examples

### React Native Integration
```javascript
// Get available time slots
const getAvailableSlots = async (courtId, date) => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/bookings/available-slots?courtId=${courtId}&date=${date}`,
      {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }
    );
    return await response.json();
  } catch (error) {
    console.error('Error getting slots:', error);
    throw error;
  }
};

// Create booking
const createBooking = async (bookingData) => {
  try {
    const response = await fetch('http://localhost:8080/api/bookings', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(bookingData)
    });
    return await response.json();
  } catch (error) {
    console.error('Error creating booking:', error);
    throw error;
  }
};

// Check availability
const checkAvailability = async (courtId, date, startTime, endTime) => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/bookings/check-availability?courtId=${courtId}&date=${date}&startTime=${startTime}&endTime=${endTime}`
    );
    return await response.json();
  } catch (error) {
    console.error('Error checking availability:', error);
    throw error;
  }
};
```

### Frontend Workflow
1. **Select Court**: User selects a court from search results
2. **Choose Date**: User picks a booking date
3. **Load Available Slots**: Call `/available-slots` to get available time slots
4. **Select Time Slots**: User selects desired start and end times
5. **Check Availability**: Optional - call `/check-availability` to verify
6. **Create Booking**: Call `/` to create the booking
7. **Confirmation**: Display booking details and confirmation

## Security Features

- **JWT Authentication**: All booking endpoints require valid JWT token
- **Role-based Access**: Only users with `PLAYER` role can create bookings
- **User Isolation**: Users can only view/manage their own bookings
- **Time Slot Validation**: Prevents double-booking and invalid time slots
- **Input Validation**: Comprehensive validation of all input parameters

## Performance Considerations

1. **Database Indexing**: Indexes on court_id, booking_date, and player_id
2. **Caching**: Cache available time slots for frequently accessed courts
3. **Optimized Queries**: Efficient queries for time slot availability checking
4. **Pagination**: Support for large booking lists
5. **Concurrent Booking Handling**: Prevents race conditions in booking creation
