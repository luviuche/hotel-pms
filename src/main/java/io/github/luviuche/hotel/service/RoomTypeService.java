package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.RoomType;
import io.github.luviuche.hotel.exception.BusinessRuleException;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomType> findAll() {
        return roomTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RoomType findById(Long id) {
        return roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomType", id));
    }

    public RoomType create(RoomType roomType) {
        if (roomTypeRepository.existsByName(roomType.getName())) {
            throw new BusinessRuleException("A room type named '" + roomType.getName() + "' already exists.");
        }
        roomType.setId(null);
        return roomTypeRepository.save(roomType);
    }

    public RoomType update(Long id, RoomType data) {
        RoomType existing = findById(id);
        if (!existing.getName().equals(data.getName())
                && roomTypeRepository.existsByName(data.getName())) {
            throw new BusinessRuleException("A room type named '" + data.getName() + "' already exists.");
        }
        existing.setName(data.getName());
        existing.setDescription(data.getDescription());
        return roomTypeRepository.save(existing);
    }

    public void delete(Long id) {
        if (!roomTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("RoomType", id);
        }
        roomTypeRepository.deleteById(id);
    }
}
