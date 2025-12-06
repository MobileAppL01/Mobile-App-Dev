package Bookington2.demo.service;

import Bookington2.demo.dto.CourtDTO;
import Bookington2.demo.entity.Court;
import Bookington2.demo.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {
    @Autowired
    private CourtRepository courtRepository;

    public List<CourtDTO> getListCourtByLocationId(Integer locationId){
        return courtRepository.findAllByLocation_Id(locationId).stream().map(this::toCourtDTO).toList();
    }

    private CourtDTO toCourtDTO(Court court){
        return new CourtDTO(court.getId(),court.getName());
    }
}
