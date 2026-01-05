# Court Search API - Location Filtering

## Base URL
```
http://localhost:8080/api/courts
```

## Endpoints

### 1. Search Courts with Location Filters
**GET** `/search`

Search courts by keyword, location, and price range.

**Parameters:**
- `keyword` (optional): Search term for court name or address
- `province` (optional): Filter by province/city
- `district` (optional): Filter by district  
- `minPrice` (optional): Minimum price per hour
- `maxPrice` (optional): Maximum price per hour
- `page` (default: 0): Page number
- `size` (default: 10): Page size

**Example Requests:**
```bash
# Search by keyword
GET /api/courts/search?keyword=badminton

# Filter by province
GET /api/courts/search?province=HCMC

# Filter by district
GET /api/courts/search?district=Binh%20Thanh

# Price range filter
GET /api/courts/search?minPrice=50000&maxPrice=150000

# Combined filters
GET /api/courts/search?keyword=court&province=HCMC&district=Binh%20Thanh&minPrice=50000
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Thanh Da Badminton Court",
    "address": "353/7A Binh Quoi, Ward 28, Binh Thanh, HCMC",
    "description": "Professional badminton court",
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

### 2. Get Available Provinces
**GET** `/provinces`

Get list of all available provinces.

**Response:**
```json
["HCMC", "Hanoi", "Da Nang"]
```

### 3. Get Districts by Province
**GET** `/districts?province={province}`

Get districts for a specific province.

**Example:**
```bash
GET /api/courts/districts?province=HCMC
```

**Response:**
```json
["Quan 1", "Quan 3", "Binh Thanh", "Phu Nhuan"]
```

### 4. Get Court Details
**GET** `/{courtId}`

Get detailed information about a specific court.

**Example:**
```bash
GET /api/courts/1
```

## Integration with Frontend

### React Native Example:
```javascript
// Search courts
const searchCourts = async (filters) => {
  const params = new URLSearchParams();
  if (filters.keyword) params.append('keyword', filters.keyword);
  if (filters.province) params.append('province', filters.province);
  if (filters.district) params.append('district', filters.district);
  if (filters.minPrice) params.append('minPrice', filters.minPrice);
  if (filters.maxPrice) params.append('maxPrice', filters.maxPrice);

  const response = await fetch(`http://localhost:8080/api/courts/search?${params}`);
  return await response.json();
};

// Get provinces
const getProvinces = async () => {
  const response = await fetch('http://localhost:8080/api/courts/provinces');
  return await response.json();
};
```

## Features Implemented

✅ **Keyword Search**: Search by court name, location name, or address
✅ **Province Filter**: Filter by province/city
✅ **District Filter**: Filter by district
✅ **Price Range**: Filter by minimum and maximum price
✅ **Pagination**: Support for page and size parameters
✅ **Location Data**: Extract province/district from addresses
✅ **Court Details**: Get detailed court information
