package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {

    // Underscore: forces the reservation.id / room.id traversal (avoids clashing with the getters).
    List<ReservationRoom> findByReservation_Id(Long reservationId);

    List<ReservationRoom> findByRoom_Id(Long roomId);
}
