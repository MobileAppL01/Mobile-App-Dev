package Bookington2.demo.controller;

import Bookington2.demo.dto.owner.CourtRevenueStats;
import Bookington2.demo.dto.owner.LocationRevenueResponse;
import Bookington2.demo.dto.owner.RevenueStatisticsResponse;
import Bookington2.demo.dto.request.APIResponse;
import Bookington2.demo.entity.User;
import Bookington2.demo.service.OwnerBookingService;
import Bookington2.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin API", description = "API cho quản trị viên")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private OwnerBookingService ownerBookingService;

    // Get all owners (for admin dashboard to list them)
    @GetMapping("/owners")
    @Operation(summary = "Lấy danh sách chủ sân", description = "Trả về danh sách tất cả người dùng có role OWNER")
    public ResponseEntity<APIResponse<List<User>>> getAllOwners() {
        List<User> owners = userService.getAllOwners();
        return ResponseEntity.ok(APIResponse.<List<User>>builder()
                .code(1000)
                .message("Lấy danh sách chủ sân thành công")
                .result(owners)
                .build());
    }

    // Get overall revenue for an owner
    @GetMapping("/owners/{ownerId}/revenue")
    @Operation(summary = "Xem tổng doanh thu của chủ sân", description = "Theo tháng/năm")
    public ResponseEntity<APIResponse<RevenueStatisticsResponse>> getOwnerRevenue(
            @PathVariable Integer ownerId,
            @Parameter(description = "Tháng (1-12)") @RequestParam int month,
            @Parameter(description = "Năm") @RequestParam int year) {
        
        RevenueStatisticsResponse stats = ownerBookingService.getRevenueStatistics(ownerId, month, year);
        return ResponseEntity.ok(APIResponse.<RevenueStatisticsResponse>builder()
                .code(1000)
                .message("Lấy thống kê doanh thu thành công")
                .result(stats)
                .build());
    }

    // Get revenue per court for an owner
    @GetMapping("/owners/{ownerId}/revenue/courts")
    @Operation(summary = "Xem doanh thu chi tiết theo từng sân", description = "Của một chủ sân cụ thể")
    public ResponseEntity<APIResponse<List<CourtRevenueStats>>> getOwnerCourtRevenue(
            @PathVariable Integer ownerId,
            @Parameter(description = "Tháng (1-12)") @RequestParam int month,
            @Parameter(description = "Năm") @RequestParam int year) {
        
        List<CourtRevenueStats> stats = ownerBookingService.getCourtRevenueStatistics(ownerId, month, year);
        return ResponseEntity.ok(APIResponse.<List<CourtRevenueStats>>builder()
                .code(1000)
                .message("Lấy thống kê doanh thu theo sân thành công")
                .result(stats)
                .build());
    }

    // Get revenue per location for an owner
    @GetMapping("/owners/{ownerId}/revenue/locations")
    @Operation(summary = "Xem doanh thu chi tiết theo cơ sở", description = "Của một chủ sân cụ thể")
    public ResponseEntity<APIResponse<List<LocationRevenueResponse>>> getOwnerLocationRevenue(
            @PathVariable Integer ownerId,
            @Parameter(description = "Tháng (1-12)") @RequestParam int month,
            @Parameter(description = "Năm") @RequestParam int year) {
        
        List<LocationRevenueResponse> stats = ownerBookingService.getRevenueByLocation(ownerId, null, month, year, null);
        return ResponseEntity.ok(APIResponse.<List<LocationRevenueResponse>>builder()
                .code(1000)
                .message("Lấy thống kê doanh thu theo cơ sở thành công")
                .result(stats)
                .build());
    }
}
