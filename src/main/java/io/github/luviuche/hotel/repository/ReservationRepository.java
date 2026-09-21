package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.Reservation;
import io.github.luviuche.hotel.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Underscore: forces the guest.id traversal (avoids clashing with getGuestId()).
    List<Reservation> findByGuest_Id(Long guestId);

    List<Reservation> findByStatus(ReservationStatus status);
}
