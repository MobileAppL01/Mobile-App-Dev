package Bookington2.demo.controller;

import Bookington2.demo.dto.CourtDTO;
import Bookington2.demo.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/court")
public class CourtController {
    @Autowired
    CourtService courtService;

    @GetMapping("/{locationId}")
    public List<CourtDTO> getCourts(@PathVariable Integer locationId){
        return courtService.getListCourtByLocationId(locationId);
    }
}
