package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.Amenity;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.AmenityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @Transactional(readOnly = true)
    public List<Amenity> findAll() {
        return amenityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Amenity findById(Long id) {
        return amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity", id));
    }

    public Amenity create(Amenity amenity) {
        if (amenity.getActive() == null) {
            amenity.setActive(true);
        }
        amenity.setId(null);
        return amenityRepository.save(amenity);
    }

    public Amenity update(Long id, Amenity data) {
        Amenity existing = findById(id);
        existing.setName(data.getName());
        existing.setType(data.getType());
        existing.setDescription(data.getDescription());
        existing.setPrice(data.getPrice());
        existing.setActive(data.getActive() != null ? data.getActive() : existing.getActive());
        return amenityRepository.save(existing);
    }

    public void delete(Long id) {
        if (!amenityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Amenity", id);
        }
        amenityRepository.deleteById(id);
    }
}
