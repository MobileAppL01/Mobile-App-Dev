# API Documentation - Court Search and Booking

## Base URL
```
http://localhost:8080/api/public
```

## Authentication
Most endpoints require JWT authentication with `PLAYER` role. Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## Endpoints

### 1. Search Courts
**GET** `/courts/search`

Search for courts with various filters.

**Query Parameters:**
- `keyword` (optional): Search term for court name or address
- `province` (optional): Filter by province/city
- `district` (optional): Filter by district
- `minPrice` (optional): Minimum price per hour
- `maxPrice` (optional): Maximum price per hour
- `page` (optional, default: 0): Page number (0-based)
- `size` (optional, default: 10): Page size

**Example Request:**
```bash
GET /api/public/courts/search?keyword=badminton&province=HCMC&minPrice=50000&maxPrice=150000&page=0&size=10
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Thanh Da Badminton Court",
    "address": "353/7A Binh Quoi, Ward 28, Binh Thanh, HCMC",
    "description": "Professional badminton court with high-quality facilities",
    "image": "court1.jpg",
    "rating": 4.5,
    "pricePerHour": 70000,
    "openTime": "06:00",
    "closeTime": "22:00",
    "status": "OPEN",
    "locationId": 1
  }
]
```

### 2. Get Court Details
**GET** `/courts/{courtId}`

Get detailed information about a specific court.

**Path Parameters:**
- `courtId`: ID of the court

**Example Request:**
```bash
GET /api/public/courts/1
```

**Response:**
```json
{
  "id": 1,
  "name": "Thanh Da Badminton Court",
  "address": "353/7A Binh Quoi, Ward 28, Binh Thanh, HCMC",
  "description": "Professional badminton court with high-quality facilities",
  "image": "court1.jpg",
  "rating": 4.5,
  "pricePerHour": 70000,
  "openTime": "06:00",
  "closeTime": "22:00",
  "status": "OPEN",
  "locationId": 1
}
```

### 3. Get Available Time Slots
**GET** `/courts/{courtId}/available-slots`

Get available time slots for a court on a specific date.

**Path Parameters:**
- `courtId`: ID of the court

**Query Parameters:**
- `date`: Date for booking (format: YYYY-MM-DD)

**Example Request:**
```bash
GET /api/public/courts/1/available-slots?date=2024-01-15
```

**Response:**
```json
[
  {
    "startHour": 6,
    "endHour": 7,
    "price": 63000,
    "available": true
  },
  {
    "startHour": 7,
    "endHour": 8,
    "price": 63000,
    "available": true
  },
  {
    "startHour": 17,
    "endHour": 18,
    "price": 84000,
    "available": true
  }
]
```

### 4. Create Booking
**POST** `/bookings`

Create a new booking for a court. Requires `PLAYER` role.

**Request Body:**
```json
{
  "courtId": 1,
  "bookingDate": "2024-01-15",
  "startHours": [17, 18],
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
  "startHours": [17, 18],
  "totalPrice": 168000,
  "status": "PENDING",
  "paymentMethod": "CASH",
  "createdAt": "2024-01-10T10:30:00",
  "notes": "Birthday party booking"
}
```

### 5. Get Booking Details
**GET** `/bookings/{bookingId}`

Get details of a specific booking. Requires `PLAYER` role.

**Path Parameters:**
- `bookingId`: ID of the booking

**Example Request:**
```bash
GET /api/public/bookings/123
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
  "startHours": [17, 18],
  "totalPrice": 168000,
  "status": "PENDING",
  "paymentMethod": "CASH",
  "createdAt": "2024-01-10T10:30:00",
  "notes": "Birthday party booking"
}
```

### 6. Cancel Booking
**DELETE** `/bookings/{bookingId}`

Cancel a specific booking. Requires `PLAYER` role.

**Path Parameters:**
- `bookingId`: ID of the booking

**Example Request:**
```bash
DELETE /api/public/bookings/123
```

**Response:** 204 No Content

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Court ID is required",
  "path": "/api/public/bookings"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/public/bookings"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Court not found with id: 999",
  "path": "/api/public/courts/999"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Time slot 17:00-18:00 is already booked",
  "path": "/api/public/bookings"
}
```

## Pricing Logic

The system implements dynamic pricing based on time slots:
- **Peak hours (17:00-20:00)**: Base price × 1.2
- **Morning hours (06:00-09:00)**: Base price × 0.9
- **Regular hours**: Base price

## Booking Status Values

- `PENDING`: Booking is pending confirmation
- `CONFIRMED`: Booking is confirmed
- `COMPLETED`: Booking has been completed
- `CANCELLED`: Booking has been cancelled

## Testing with Postman

You can test these APIs using Postman with the following collection:

1. **Search Courts**: GET request with query parameters
2. **Get Court Details**: GET request with path parameter
3. **Get Available Slots**: GET request with path and query parameters
4. **Create Booking**: POST request with JSON body and JWT token
5. **Get Booking Details**: GET request with path parameter and JWT token
6. **Cancel Booking**: DELETE request with path parameter and JWT token

## Integration with Frontend

The frontend should:
1. Use the search endpoint to display courts with filters
2. Show available time slots when user selects a court and date
3. Create bookings with the calculated total price
4. Display booking details and allow cancellation
5. Handle authentication and authorization properly
