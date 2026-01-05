# Court Search API Documentation

## Base URL
```
http://localhost:8080/api/courts
```

## Overview
This API provides comprehensive court search functionality with location-based filtering, sorting, and pagination capabilities.

## Endpoints

### 1. Search Courts (GET)
**GET** `/search`

Search courts with various filters using query parameters.

**Query Parameters:**
- `keyword` (optional): Search term for court name, location name, or address
- `province` (optional): Filter by province/city
- `district` (optional): Filter by district
- `minPrice` (optional): Minimum price per hour
- `maxPrice` (optional): Maximum price per hour
- `page` (optional, default: 0): Page number (0-based)
- `size` (optional, default: 10): Page size
- `sortBy` (optional, default: "name"): Sort field (name, price, rating)
- `sortDirection` (optional, default: "asc"): Sort direction (asc, desc)

**Example Requests:**
```bash
# Basic search with keyword
GET /api/courts/search?keyword=badminton

# Search by location
GET /api/courts/search?province=HCMC&district=Binh%20Thanh

# Search with price range
GET /api/courts/search?minPrice=50000&maxPrice=150000

# Combined search with sorting
GET /api/courts/search?keyword=court&province=HCMC&sortBy=price&sortDirection=asc&page=0&size=5
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
    "locationId": 1,
    "province": "HCMC",
    "district": "Binh Thanh"
  }
]
```

### 2. Search Courts (POST)
**POST** `/search`

Search courts using POST method with complex filters in request body.

**Request Body:**
```json
{
  "keyword": "badminton",
  "province": "HCMC",
  "district": "Binh Thanh",
  "minPrice": 50000,
  "maxPrice": 150000,
  "page": 0,
  "size": 10,
  "sortBy": "rating",
  "sortDirection": "desc"
}
```

**Response:** Same as GET search endpoint

### 3. Get Available Provinces
**GET** `/provinces`

Get list of all available provinces/cities in the system.

**Response:**
```json
[
  "HCMC",
  "Hanoi",
  "Da Nang",
  "Can Tho"
]
```

### 4. Get Districts by Province
**GET** `/districts?province={provinceName}`

Get list of districts for a specific province.

**Query Parameters:**
- `province`: Province name

**Example Request:**
```bash
GET /api/courts/districts?province=HCMC
```

**Response:**
```json
[
  "Quan 1",
  "Quan 3",
  "Binh Thanh",
  "Phu Nhuan",
  "Go Vap"
]
```

### 5. Get Court Details
**GET** `/{courtId}`

Get detailed information about a specific court.

**Path Parameters:**
- `courtId`: ID of the court

**Example Request:**
```bash
GET /api/courts/1
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
  "locationId": 1,
  "province": "HCMC",
  "district": "Binh Thanh"
}
```

### 6. Get Popular Courts
**GET** `/popular`

Get list of popular courts based on rating and availability.

**Query Parameters:**
- `limit` (optional, default: 10): Maximum number of results

**Example Request:**
```bash
GET /api/courts/popular?limit=5
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Thanh Da Badminton Court",
    "address": "353/7A Binh Quoi, Ward 28, Binh Thanh, HCMC",
    "rating": 4.8,
    "pricePerHour": 70000,
    "province": "HCMC",
    "district": "Binh Thanh"
  }
]
```

### 7. Get Nearby Courts
**GET** `/nearby`

Get courts near a specific location.

**Query Parameters:**
- `latitude` (optional): Latitude coordinate
- `longitude` (optional): Longitude coordinate
- `address` (optional): Address for location-based search
- `radius` (optional, default: 10): Search radius in kilometers
- `limit` (optional, default: 10): Maximum number of results

**Example Requests:**
```bash
# Search by coordinates
GET /api/courts/nearby?latitude=10.8231&longitude=106.6297&radius=5

# Search by address
GET /api/courts/nearby?address=Binh%20Thanh%20HCMC&limit=5
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Thanh Da Badminton Court",
    "address": "353/7A Binh Quoi, Ward 28, Binh Thanh, HCMC",
    "rating": 4.5,
    "pricePerHour": 70000,
    "province": "HCMC",
    "district": "Binh Thanh"
  }
]
```

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid parameter value",
  "path": "/api/courts/search"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Court not found with id: 999",
  "path": "/api/courts/999"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Database error occurred",
  "path": "/api/courts/search"
}
```

## Search Features

### 1. Keyword Search
- Searches in court name, location name, address, and description
- Case-insensitive search
- Supports partial matching

### 2. Location Filtering
- **Province/City**: Filter by province or city name
- **District**: Filter by district name
- **Address**: Full-text search in address field

### 3. Price Filtering
- **Min Price**: Minimum price per hour
- **Max Price**: Maximum price per hour
- Prices are in VND (Vietnamese Dong)

### 4. Sorting Options
- **name**: Sort by court name (default)
- **price**: Sort by price per hour
- **rating**: Sort by rating
- **asc**: Ascending order (default)
- **desc**: Descending order

### 5. Pagination
- **page**: Page number (0-based, default: 0)
- **size**: Number of items per page (default: 10, max: 100)

## Integration Examples

### React Native Integration
```javascript
// Search courts with filters
const searchCourts = async (filters) => {
  try {
    const params = new URLSearchParams();
    if (filters.keyword) params.append('keyword', filters.keyword);
    if (filters.province) params.append('province', filters.province);
    if (filters.district) params.append('district', filters.district);
    if (filters.minPrice) params.append('minPrice', filters.minPrice);
    if (filters.maxPrice) params.append('maxPrice', filters.maxPrice);
    params.append('page', filters.page || 0);
    params.append('size', filters.size || 10);

    const response = await fetch(`http://localhost:8080/api/courts/search?${params}`);
    const courts = await response.json();
    return courts;
  } catch (error) {
    console.error('Error searching courts:', error);
    throw error;
  }
};

// Get provinces for dropdown
const getProvinces = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/courts/provinces');
    const provinces = await response.json();
    return provinces;
  } catch (error) {
    console.error('Error getting provinces:', error);
    throw error;
  }
};
```

### Postman Collection
1. **Search Courts**: GET with various query parameters
2. **Get Provinces**: GET to populate dropdown
3. **Get Districts**: GET with province parameter
4. **Get Court Details**: GET with court ID
5. **Get Popular Courts**: GET with limit parameter
6. **Get Nearby Courts**: GET with location parameters

## Performance Considerations

1. **Indexing**: Ensure database indexes on:
   - Location address (for text search)
   - Price per hour (for range queries)
   - Rating (for sorting)

2. **Caching**: Consider caching:
   - Popular courts list
   - Province/district lists
   - Search results for common queries

3. **Pagination**: Always use pagination for large datasets
4. **Rate Limiting**: Implement rate limiting for public endpoints

## Future Enhancements

1. **Geospatial Search**: Full GPS-based search with distance calculation
2. **Advanced Filters**: More specific filters (facilities, amenities, etc.)
3. **Search Analytics**: Track search patterns and popular queries
4. **Autocomplete**: Implement autocomplete for search suggestions
5. **Saved Searches**: Allow users to save search preferences
