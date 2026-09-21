package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.ReservationAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationAmenityRepository extends JpaRepository<ReservationAmenity, Long> {

    // Underscore: forces the reservation.id / amenity.id traversal (avoids clashing with the getters).
    List<ReservationAmenity> findByReservation_Id(Long reservationId);

    List<ReservationAmenity> findByAmenity_Id(Long amenityId);
}
