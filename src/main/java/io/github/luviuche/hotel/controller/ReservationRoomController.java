package io.github.luviuche.hotel.controller;

import io.github.luviuche.hotel.entity.ReservationRoom;
import io.github.luviuche.hotel.service.ReservationRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation-rooms")
public class ReservationRoomController {

    private final ReservationRoomService reservationRoomService;

    public ReservationRoomController(ReservationRoomService reservationRoomService) {
        this.reservationRoomService = reservationRoomService;
    }

    @GetMapping
    public List<ReservationRoom> findAll() {
        return reservationRoomService.findAll();
    }

    @GetMapping("/{id}")
    public ReservationRoom findById(@PathVariable Long id) {
        return reservationRoomService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationRoom create(@RequestBody ReservationRoom line) {
        return reservationRoomService.create(line);
    }

    @PutMapping("/{id}")
    public ReservationRoom update(@PathVariable Long id, @RequestBody ReservationRoom line) {
        return reservationRoomService.update(id, line);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reservationRoomService.delete(id);
    }
}
