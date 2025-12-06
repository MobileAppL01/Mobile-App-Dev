package Bookington2.demo.service;

import Bookington2.demo.dto.LocationDTO;
import Bookington2.demo.dto.OpenTimeDTO;
import Bookington2.demo.entity.Location;
import Bookington2.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;
    public List<LocationDTO> getAllLocations(){
        return locationRepository.findAll().stream().map(this::toLocationDto).toList();
    }
    private LocationDTO toLocationDto(Location location){
        return new LocationDTO(location.getId(),location.getName(),location.getAddress(),location.getRating(),location.getPricePerHour());
    }
    public List<LocationDTO> getLocationsByAddress(String address){
        return locationRepository.searchByAddress(address).stream().map(this::toLocationDto).toList();
    }

    public OpenTimeDTO getOpenTimeByLocationId(Integer id){
        return locationRepository.findLocationById(id);
    }
}
