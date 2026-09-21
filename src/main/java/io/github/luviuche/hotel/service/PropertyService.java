package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.HotelChain;
import io.github.luviuche.hotel.entity.Property;
import io.github.luviuche.hotel.enums.ActivationStatus;
import io.github.luviuche.hotel.exception.BusinessRuleException;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.HotelChainRepository;
import io.github.luviuche.hotel.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final HotelChainRepository hotelChainRepository;

    public PropertyService(PropertyRepository propertyRepository, HotelChainRepository hotelChainRepository) {
        this.propertyRepository = propertyRepository;
        this.hotelChainRepository = hotelChainRepository;
    }

    @Transactional(readOnly = true)
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Property findById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property", id));
    }

    public Property create(Property property) {
        property.setHotelChain(resolveHotelChain(property.getHotelChainId()));
        if (property.getStatus() == null) {
            property.setStatus(ActivationStatus.ACTIVE);
        }
        property.setId(null);
        return propertyRepository.save(property);
    }

    public Property update(Long id, Property data) {
        Property existing = findById(id);
        existing.setHotelChain(resolveHotelChain(data.getHotelChainId()));
        existing.setName(data.getName());
        existing.setAddress(data.getAddress());
        existing.setCity(data.getCity());
        existing.setPhone(data.getPhone());
        existing.setEmail(data.getEmail());
        existing.setCategory(data.getCategory());
        existing.setStatus(data.getStatus() != null ? data.getStatus() : existing.getStatus());
        return propertyRepository.save(existing);
    }

    public void delete(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Property", id);
        }
        propertyRepository.deleteById(id);
    }

    private HotelChain resolveHotelChain(Long hotelChainId) {
        if (hotelChainId == null) {
            throw new BusinessRuleException("A property requires a hotelChainId.");
        }
        return hotelChainRepository.findById(hotelChainId)
                .orElseThrow(() -> new ResourceNotFoundException("HotelChain", hotelChainId));
    }
}
