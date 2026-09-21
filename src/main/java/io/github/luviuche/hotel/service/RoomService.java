package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.Property;
import io.github.luviuche.hotel.entity.Room;
import io.github.luviuche.hotel.entity.RoomType;
import io.github.luviuche.hotel.enums.RoomStatus;
import io.github.luviuche.hotel.exception.BusinessRuleException;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.PropertyRepository;
import io.github.luviuche.hotel.repository.RoomRepository;
import io.github.luviuche.hotel.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final PropertyRepository propertyRepository;
    private final RoomTypeRepository roomTypeRepository;

    public RoomService(RoomRepository roomRepository,
                       PropertyRepository propertyRepository,
                       RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.propertyRepository = propertyRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));
    }

    public Room create(Room room) {
        Property property = resolveProperty(room.getPropertyId());
        room.setProperty(property);
        room.setRoomType(resolveRoomType(room.getRoomTypeId()));
        if (roomRepository.existsByProperty_IdAndNumber(property.getId(), room.getNumber())) {
            throw new BusinessRuleException(
                    "The property already has a room numbered '" + room.getNumber() + "'.");
        }
        applyDefaults(room);
        room.setId(null);
        return roomRepository.save(room);
    }

    public Room update(Long id, Room data) {
        Room existing = findById(id);
        Property property = resolveProperty(data.getPropertyId());
        boolean numberOrPropertyChanged = !existing.getProperty().getId().equals(property.getId())
                || !existing.getNumber().equals(data.getNumber());
        if (numberOrPropertyChanged
                && roomRepository.existsByProperty_IdAndNumber(property.getId(), data.getNumber())) {
            throw new BusinessRuleException(
                    "The property already has a room numbered '" + data.getNumber() + "'.");
        }
        existing.setProperty(property);
        existing.setRoomType(resolveRoomType(data.getRoomTypeId()));
        existing.setNumber(data.getNumber());
        existing.setFloor(data.getFloor());
        existing.setCapacity(data.getCapacity());
        existing.setPricePerNight(data.getPricePerNight());
        existing.setStatus(data.getStatus() != null ? data.getStatus() : existing.getStatus());
        existing.setAvailable(data.getAvailable() != null ? data.getAvailable() : existing.getAvailable());
        return roomRepository.save(existing);
    }

    public void delete(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room", id);
        }
        roomRepository.deleteById(id);
    }

    private void applyDefaults(Room room) {
        if (room.getStatus() == null) {
            room.setStatus(RoomStatus.AVAILABLE);
        }
        if (room.getAvailable() == null) {
            room.setAvailable(room.getStatus() == RoomStatus.AVAILABLE);
        }
    }

    private Property resolveProperty(Long propertyId) {
        if (propertyId == null) {
            throw new BusinessRuleException("A room requires a propertyId.");
        }
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property", propertyId));
    }

    private RoomType resolveRoomType(Long roomTypeId) {
        if (roomTypeId == null) {
            throw new BusinessRuleException("A room requires a roomTypeId.");
        }
        return roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("RoomType", roomTypeId));
    }
}
