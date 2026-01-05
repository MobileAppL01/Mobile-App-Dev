# Enhanced Court Search API - Price and Location Filtering

## Base URL
```
http://localhost:8080/api/courts
```

## Overview
This API provides comprehensive court search functionality with advanced filtering by price range, province/city, district/ward, and combined location searches.

## Search Endpoints

### 1. Comprehensive Search
**GET** `/search`

Search courts with all available filters combined.

**Parameters:**
- `keyword` (optional): Search term for court name, address, or description
- `province` (optional): Filter by province/city
- `district` (optional): Filter by district/ward
- `minPrice` (optional): Minimum price per hour
- `maxPrice` (optional): Maximum price per hour
- `page` (default: 0): Page number
- `size` (default: 10): Page size

**Example Requests:**
```bash
# Search by price range only
GET /api/courts/search?minPrice=50000&maxPrice=150000

# Search by province only
GET /api/courts/search?province=HCMC

# Search by district only
GET /api/courts/search?district=Binh%20Thanh

# Combined location search
GET /api/courts/search?province=HCMC&district=Binh%20Thanh

# All filters combined
GET /api/courts/search?keyword=badminton&province=HCMC&district=Binh%20Thanh&minPrice=50000&maxPrice=150000
```

### 2. Price Range Search
**GET** `/search/by-price`

Search courts within a specific price range.

**Parameters:**
- `minPrice` (optional): Minimum price per hour
- `maxPrice` (optional): Maximum price per hour
- `page` (default: 0): Page number
- `size` (default: 10): Page size

**Example:**
```bash
GET /api/courts/search/by-price?minPrice=50000&maxPrice=100000
```

### 3. Province/City Search
**GET** `/search/by-province`

Search courts in a specific province or city.

**Parameters:**
- `province`: Province/city name (required)
- `page` (default: 0): Page number
- `size` (default: 10): Page size

**Example:**
```bash
GET /api/courts/search/by-province?province=HCMC
```

### 4. District/Ward Search
**GET** `/search/by-district`

Search courts in a specific district or ward.

**Parameters:**
- `district`: District/ward name (required)
- `page` (default: 0): Page number
- `size` (default: 10): Page size

**Example:**
```bash
GET /api/courts/search/by-district?district=Binh%20Thanh
```

### 5. Combined Location Search
**GET** `/search/by-location`

Search courts in a specific province AND district.

**Parameters:**
- `province`: Province/city name (required)
- `district`: District/ward name (required)
- `page` (default: 0): Page number
- `size` (default: 10): Page size

**Example:**
```bash
GET /api/courts/search/by-location?province=HCMC&district=Binh%20Thanh
```

### 6. Keyword Search
**GET** `/search/by-keyword`

Search courts by name, address, or description.

**Parameters:**
- `keyword`: Search term (required)
- `page` (default: 0): Page number
- `size` (default: 10): Page size

**Example:**
```bash
GET /api/courts/search/by-keyword?keyword=badminton
```

## Location Data Endpoints

### 1. Get All Provinces
**GET** `/provinces`

Get list of all available provinces/cities.

**Response:**
```json
["Binh Dinh", "Da Nang", "HCMC", "Hanoi", "Can Tho"]
```

### 2. Get Districts by Province
**GET** `/districts?province={province}`

Get districts for a specific province.

**Example:**
```bash
GET /api/courts/districts?province=HCMC
```

**Response:**
```json
["Quan 1", "Quan 3", "Binh Thanh", "Phu Nhuan", "Go Vap"]
```

### 3. Get All Districts
**GET** `/districts/all`

Get list of all available districts/wards.

**Response:**
```json
["Quan 1", "Quan 3", "Binh Thanh", "Phu Nhuan", "Go Vap", "Hai Chau", "Lien Chieu"]
```

## Response Format

All search endpoints return the same response format:

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

## Search Features

### 1. Price Filtering
- **Minimum Price**: Filter courts with price >= minPrice
- **Maximum Price**: Filter courts with price <= maxPrice
- **Price Range**: Combine both for range filtering
- **Currency**: Prices are in VND (Vietnamese Dong)

### 2. Location Filtering
- **Province/City**: Filter by province or city name
- **District/Ward**: Filter by district or ward name
- **Combined**: Filter by both province AND district
- **Case Insensitive**: Search is case insensitive
- **Partial Match**: Supports partial string matching

### 3. Keyword Search
- **Court Name**: Search in court names
- **Address**: Search in full address
- **Description**: Search in court descriptions
- **Location Name**: Search in location names

### 4. Pagination
- **Page-based**: 0-based page numbering
- **Page Size**: Configurable (default: 10)
- **Sorting**: Results can be sorted by various fields

## Integration Examples

### React Native Integration
```javascript
// Search by price range
const searchByPrice = async (minPrice, maxPrice) => {
  const params = new URLSearchParams();
  if (minPrice) params.append('minPrice', minPrice);
  if (maxPrice) params.append('maxPrice', maxPrice);
  
  const response = await fetch(`/api/courts/search/by-price?${params}`);
  return await response.json();
};

// Search by province
const searchByProvince = async (province) => {
  const response = await fetch(`/api/courts/search/by-province?province=${encodeURIComponent(province)}`);
  return await response.json();
};

// Combined search
const searchCourts = async (filters) => {
  const params = new URLSearchParams();
  if (filters.keyword) params.append('keyword', filters.keyword);
  if (filters.province) params.append('province', filters.province);
  if (filters.district) params.append('district', filters.district);
  if (filters.minPrice) params.append('minPrice', filters.minPrice);
  if (filters.maxPrice) params.append('maxPrice', filters.maxPrice);

  const response = await fetch(`/api/courts/search?${params}`);
  return await response.json();
};

// Get provinces for dropdown
const getProvinces = async () => {
  const response = await fetch('/api/courts/provinces');
  return await response.json();
};

// Get districts by province
const getDistricts = async (province) => {
  const response = await fetch(`/api/courts/districts?province=${encodeURIComponent(province)}`);
  return await response.json();
};
```

### Frontend Search Workflow
1. **Load Provinces**: Call `/provinces` to populate province dropdown
2. **Load Districts**: When province selected, call `/districts?province={province}`
3. **Apply Filters**: Use appropriate endpoint based on active filters
4. **Display Results**: Show paginated results with price and location info
5. **Refine Search**: Allow users to combine multiple filters

## Advanced Search Combinations

### 1. Budget Search in Specific Area
```bash
GET /api/courts/search?province=HCMC&minPrice=50000&maxPrice=100000
```

### 2. Premium Courts in District
```bash
GET /api/courts/search?district=Binh%20Thanh&minPrice=150000
```

### 3. Keyword Search with Location
```bash
GET /api/courts/search?keyword=badminton&province=HCMC&district=Phu%20Nhuan
```

### 4. Cheap Courts Everywhere
```bash
GET /api/courts/search/by-price?maxPrice=80000
```

## Performance Considerations

1. **Database Indexing**: Ensure indexes on:
   - Location address (for text search)
   - Price per hour (for range queries)
   - Province/district extracted fields

2. **Caching**: Consider caching:
   - Province and district lists
   - Popular search results
   - Price range queries

3. **Pagination**: Always use pagination for large datasets
4. **Search Optimization**: Use appropriate database queries for location filtering

## Error Handling

### 400 Bad Request
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid price range: minPrice cannot be greater than maxPrice"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-10T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "No courts found for the specified criteria"
}
```

## Future Enhancements

1. **Geospatial Search**: GPS-based location search with distance calculation
2. **Advanced Filters**: More specific filters (facilities, amenities, ratings)
3. **Search Analytics**: Track popular search patterns
4. **Autocomplete**: Implement search suggestions
5. **Saved Searches**: Allow users to save search preferences
