package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.Payment;
import io.github.luviuche.hotel.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Underscore: forces the reservation.id traversal (avoids clashing with getReservationId()).
    Optional<Payment> findByReservation_Id(Long reservationId);

    boolean existsByReservation_Id(Long reservationId);

    List<Payment> findByStatus(PaymentStatus status);
}
