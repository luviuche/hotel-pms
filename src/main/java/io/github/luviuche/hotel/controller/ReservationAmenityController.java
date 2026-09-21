package io.github.luviuche.hotel.controller;

import io.github.luviuche.hotel.entity.ReservationAmenity;
import io.github.luviuche.hotel.service.ReservationAmenityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation-amenities")
public class ReservationAmenityController {

    private final ReservationAmenityService reservationAmenityService;

    public ReservationAmenityController(ReservationAmenityService reservationAmenityService) {
        this.reservationAmenityService = reservationAmenityService;
    }

    @GetMapping
    public List<ReservationAmenity> findAll() {
        return reservationAmenityService.findAll();
    }

    @GetMapping("/{id}")
    public ReservationAmenity findById(@PathVariable Long id) {
        return reservationAmenityService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationAmenity create(@RequestBody ReservationAmenity line) {
        return reservationAmenityService.create(line);
    }

    @PutMapping("/{id}")
    public ReservationAmenity update(@PathVariable Long id, @RequestBody ReservationAmenity line) {
        return reservationAmenityService.update(id, line);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reservationAmenityService.delete(id);
    }
}
