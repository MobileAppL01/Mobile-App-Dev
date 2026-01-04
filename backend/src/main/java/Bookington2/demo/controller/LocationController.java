package Bookington2.demo.controller;


import Bookington2.demo.dto.LocationDTO;
import Bookington2.demo.dto.OpenTimeDTO;
import Bookington2.demo.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    LocationService locationService;
    @PreAuthorize("hasAnyRole('PLAYER','OWNER')")
    @GetMapping
    public List<LocationDTO> getListLocation(){
        return locationService.getAllLocations();
    }

    @PreAuthorize("hasAnyRole('PLAYER','OWNER')")
    @GetMapping(params = "address")
    public List<LocationDTO> getListLocation(@RequestParam String address){
        return locationService.getLocationsByAddress(address);
    }
    @PreAuthorize("hasAnyRole('PLAYER','OWNER')")
    @GetMapping(params = "location",path = "/open-time")
    public OpenTimeDTO getListLocation(@RequestParam Integer location){
        return locationService.getOpenTimeByLocationId(location);
    }
}
