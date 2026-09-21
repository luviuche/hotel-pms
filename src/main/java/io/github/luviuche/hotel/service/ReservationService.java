package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.Reservation;
import io.github.luviuche.hotel.entity.User;
import io.github.luviuche.hotel.enums.ReservationStatus;
import io.github.luviuche.hotel.exception.BusinessRuleException;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.ReservationRepository;
import io.github.luviuche.hotel.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
    }

    public Reservation create(Reservation reservation) {
        reservation.setGuest(resolveGuest(reservation.getGuestId()));
        validateDates(reservation);
        validateTotal(reservation);
        if (reservation.getStatus() == null) {
            reservation.setStatus(ReservationStatus.PENDING);
        }
        reservation.setId(null);
        return reservationRepository.save(reservation);
    }

    public Reservation update(Long id, Reservation data) {
        Reservation existing = findById(id);
        existing.setGuest(resolveGuest(data.getGuestId()));
        existing.setCheckInDate(data.getCheckInDate());
        existing.setCheckOutDate(data.getCheckOutDate());
        existing.setStatus(data.getStatus() != null ? data.getStatus() : existing.getStatus());
        existing.setTotal(data.getTotal());
        validateDates(existing);
        validateTotal(existing);
        return reservationRepository.save(existing);
    }

    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    private void validateDates(Reservation reservation) {
        if (reservation.getCheckInDate() == null || reservation.getCheckOutDate() == null) {
            throw new BusinessRuleException("A reservation requires a check-in and a check-out date.");
        }
        if (!reservation.getCheckOutDate().isAfter(reservation.getCheckInDate())) {
            throw new BusinessRuleException("The check-out date must be after the check-in date.");
        }
    }

    private void validateTotal(Reservation reservation) {
        if (reservation.getTotal() == null || reservation.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("The reservation total cannot be negative.");
        }
    }

    private User resolveGuest(Long guestId) {
        if (guestId == null) {
            throw new BusinessRuleException("A reservation requires a guestId.");
        }
        return userRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("User", guestId));
    }
}
