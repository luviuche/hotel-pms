package io.github.luviuche.hotel.controller;

import io.github.luviuche.hotel.entity.Property;
import io.github.luviuche.hotel.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public List<Property> findAll() {
        return propertyService.findAll();
    }

    @GetMapping("/{id}")
    public Property findById(@PathVariable Long id) {
        return propertyService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Property create(@RequestBody Property property) {
        return propertyService.create(property);
    }

    @PutMapping("/{id}")
    public Property update(@PathVariable Long id, @RequestBody Property property) {
        return propertyService.update(id, property);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        propertyService.delete(id);
    }
}
