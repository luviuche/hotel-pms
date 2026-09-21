package io.github.luviuche.hotel.controller;

import io.github.luviuche.hotel.entity.Amenity;
import io.github.luviuche.hotel.service.AmenityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    public List<Amenity> findAll() {
        return amenityService.findAll();
    }

    @GetMapping("/{id}")
    public Amenity findById(@PathVariable Long id) {
        return amenityService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Amenity create(@RequestBody Amenity amenity) {
        return amenityService.create(amenity);
    }

    @PutMapping("/{id}")
    public Amenity update(@PathVariable Long id, @RequestBody Amenity amenity) {
        return amenityService.update(id, amenity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        amenityService.delete(id);
    }
}
