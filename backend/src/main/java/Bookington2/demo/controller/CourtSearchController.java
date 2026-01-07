package Bookington2.demo.controller;

import Bookington2.demo.dto.CourtSearchDTO;
import Bookington2.demo.service.CourtSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courts")
@Tag(name = "Court Search API", description = "API endpoints for searching courts with location and price filters")
public class CourtSearchController {

    @Autowired
    private CourtSearchService courtSearchService;

    @GetMapping("/search")
    @Operation(summary = "Search courts with comprehensive filters", description = "Search courts by keyword, location, and price range")
    public ResponseEntity<List<CourtSearchDTO>> searchCourts(
            @Parameter(description = "Search keyword for court name or address") @RequestParam(required = false) String keyword,

            @Parameter(description = "Province/City filter") @RequestParam(required = false) String province,

            @Parameter(description = "District/Ward filter") @RequestParam(required = false) String district,

            @Parameter(description = "Minimum price per hour") @RequestParam(required = false) Integer minPrice,

            @Parameter(description = "Maximum price per hour") @RequestParam(required = false) Integer maxPrice,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        List<CourtSearchDTO> courts = courtSearchService.searchCourts(
                keyword, province, district, minPrice, maxPrice, page, size);
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/search/by-price")
    @Operation(summary = "Search courts by price range", description = "Search courts within a specific price range")
    public ResponseEntity<List<CourtSearchDTO>> searchByPrice(
            @Parameter(description = "Minimum price per hour") @RequestParam(required = false) Integer minPrice,

            @Parameter(description = "Maximum price per hour") @RequestParam(required = false) Integer maxPrice,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        List<CourtSearchDTO> courts = courtSearchService.searchByPriceRange(minPrice, maxPrice, page, size);
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/search/by-province")
    @Operation(summary = "Search courts by province/city", description = "Search courts in a specific province or city")
    public ResponseEntity<List<CourtSearchDTO>> searchByProvince(
            @Parameter(description = "Province/City name") @RequestParam String province,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        List<CourtSearchDTO> courts = courtSearchService.searchByProvince(province, page, size);
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/search/by-district")
    @Operation(summary = "Search courts by district/ward", description = "Search courts in a specific district or ward")
    public ResponseEntity<List<CourtSearchDTO>> searchByDistrict(
            @Parameter(description = "District/Ward name") @RequestParam String district,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        List<CourtSearchDTO> courts = courtSearchService.searchByDistrict(district, page, size);
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/search/by-location")
    @Operation(summary = "Search courts by province and district", description = "Search courts in a specific province and district")
    public ResponseEntity<List<CourtSearchDTO>> searchByProvinceAndDistrict(
            @Parameter(description = "Province/City name") @RequestParam String province,

            @Parameter(description = "District/Ward name") @RequestParam String district,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        List<CourtSearchDTO> courts = courtSearchService.searchByProvinceAndDistrict(province, district, page, size);
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/search/by-keyword")
    @Operation(summary = "Search courts by keyword", description = "Search courts by name, address, or description")
    public ResponseEntity<List<CourtSearchDTO>> searchByKeyword(
            @Parameter(description = "Search keyword") @RequestParam String keyword,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        List<CourtSearchDTO> courts = courtSearchService.searchByKeyword(keyword, page, size);
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/provinces")
    @Operation(summary = "Get available provinces", description = "Get list of all available provinces/cities")
    public ResponseEntity<List<String>> getAvailableProvinces() {
        List<String> provinces = courtSearchService.getAvailableProvinces();
        return ResponseEntity.ok(provinces);
    }

    @GetMapping("/districts")
    @Operation(summary = "Get districts by province", description = "Get list of districts for a specific province")
    public ResponseEntity<List<String>> getDistrictsByProvince(
            @Parameter(description = "Province name") @RequestParam String province) {
        List<String> districts = courtSearchService.getDistrictsByProvince(province);
        return ResponseEntity.ok(districts);
    }

    @GetMapping("/districts/all")
    @Operation(summary = "Get all districts", description = "Get list of all available districts/wards")
    public ResponseEntity<List<String>> getAllDistricts() {
        List<String> districts = courtSearchService.getAllDistricts();
        return ResponseEntity.ok(districts);
    }

    @GetMapping("/{courtId}")
    @Operation(summary = "Get court details", description = "Get detailed information about a specific court")
    public ResponseEntity<CourtSearchDTO> getCourtDetails(
            @Parameter(description = "Court ID") @PathVariable Integer courtId) {

        CourtSearchDTO court = courtSearchService.getCourtDetails(courtId);
        return ResponseEntity.ok(court);
    }
}