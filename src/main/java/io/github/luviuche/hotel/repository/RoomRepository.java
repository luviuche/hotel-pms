package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.Room;
import io.github.luviuche.hotel.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Underscore: forces the property.id traversal (avoids clashing with getPropertyId()).
    List<Room> findByProperty_Id(Long propertyId);

    List<Room> findByStatus(RoomStatus status);

    List<Room> findByAvailableTrue();

    boolean existsByProperty_IdAndNumber(Long propertyId, String number);
}
