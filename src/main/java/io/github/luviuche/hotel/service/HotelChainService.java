package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.HotelChain;
import io.github.luviuche.hotel.enums.ActivationStatus;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.HotelChainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HotelChainService {

    private final HotelChainRepository hotelChainRepository;

    public HotelChainService(HotelChainRepository hotelChainRepository) {
        this.hotelChainRepository = hotelChainRepository;
    }

    @Transactional(readOnly = true)
    public List<HotelChain> findAll() {
        return hotelChainRepository.findAll();
    }

    @Transactional(readOnly = true)
    public HotelChain findById(Long id) {
        return hotelChainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HotelChain", id));
    }

    public HotelChain create(HotelChain chain) {
        if (chain.getStatus() == null) {
            chain.setStatus(ActivationStatus.ACTIVE);
        }
        chain.setId(null);
        return hotelChainRepository.save(chain);
    }

    public HotelChain update(Long id, HotelChain data) {
        HotelChain existing = findById(id);
        existing.setName(data.getName());
        existing.setDescription(data.getDescription());
        existing.setStatus(data.getStatus() != null ? data.getStatus() : existing.getStatus());
        return hotelChainRepository.save(existing);
    }

    public void delete(Long id) {
        if (!hotelChainRepository.existsById(id)) {
            throw new ResourceNotFoundException("HotelChain", id);
        }
        hotelChainRepository.deleteById(id);
    }
}
