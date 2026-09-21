package io.github.luviuche.hotel.controller;

import io.github.luviuche.hotel.entity.RoomType;
import io.github.luviuche.hotel.service.RoomTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping
    public List<RoomType> findAll() {
        return roomTypeService.findAll();
    }

    @GetMapping("/{id}")
    public RoomType findById(@PathVariable Long id) {
        return roomTypeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomType create(@RequestBody RoomType roomType) {
        return roomTypeService.create(roomType);
    }

    @PutMapping("/{id}")
    public RoomType update(@PathVariable Long id, @RequestBody RoomType roomType) {
        return roomTypeService.update(id, roomType);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roomTypeService.delete(id);
    }
}
