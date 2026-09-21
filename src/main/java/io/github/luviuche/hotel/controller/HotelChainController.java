package io.github.luviuche.hotel.controller;

import io.github.luviuche.hotel.entity.HotelChain;
import io.github.luviuche.hotel.service.HotelChainService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotel-chains")
public class HotelChainController {

    private final HotelChainService hotelChainService;

    public HotelChainController(HotelChainService hotelChainService) {
        this.hotelChainService = hotelChainService;
    }

    @GetMapping
    public List<HotelChain> findAll() {
        return hotelChainService.findAll();
    }

    @GetMapping("/{id}")
    public HotelChain findById(@PathVariable Long id) {
        return hotelChainService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HotelChain create(@RequestBody HotelChain chain) {
        return hotelChainService.create(chain);
    }

    @PutMapping("/{id}")
    public HotelChain update(@PathVariable Long id, @RequestBody HotelChain chain) {
        return hotelChainService.update(id, chain);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        hotelChainService.delete(id);
    }
}
