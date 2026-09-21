package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.Amenity;
import io.github.luviuche.hotel.entity.Reservation;
import io.github.luviuche.hotel.entity.ReservationAmenity;
import io.github.luviuche.hotel.exception.BusinessRuleException;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.AmenityRepository;
import io.github.luviuche.hotel.repository.ReservationAmenityRepository;
import io.github.luviuche.hotel.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ReservationAmenityService {

    private final ReservationAmenityRepository reservationAmenityRepository;
    private final ReservationRepository reservationRepository;
    private final AmenityRepository amenityRepository;

    public ReservationAmenityService(ReservationAmenityRepository reservationAmenityRepository,
                                     ReservationRepository reservationRepository,
                                     AmenityRepository amenityRepository) {
        this.reservationAmenityRepository = reservationAmenityRepository;
        this.reservationRepository = reservationRepository;
        this.amenityRepository = amenityRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationAmenity> findAll() {
        return reservationAmenityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ReservationAmenity findById(Long id) {
        return reservationAmenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReservationAmenity", id));
    }

    public ReservationAmenity create(ReservationAmenity line) {
        line.setReservation(resolveReservation(line.getReservationId()));
        Amenity amenity = resolveAmenity(line.getAmenityId());
        line.setAmenity(amenity);
        calculateAmounts(line, amenity);
        line.setId(null);
        return reservationAmenityRepository.save(line);
    }

    public ReservationAmenity update(Long id, ReservationAmenity data) {
        ReservationAmenity existing = findById(id);
        existing.setReservation(resolveReservation(data.getReservationId()));
        Amenity amenity = resolveAmenity(data.getAmenityId());
        existing.setAmenity(amenity);
        existing.setQuantity(data.getQuantity());
        existing.setUnitPrice(data.getUnitPrice());
        calculateAmounts(existing, amenity);
        return reservationAmenityRepository.save(existing);
    }

    public void delete(Long id) {
        if (!reservationAmenityRepository.existsById(id)) {
            throw new ResourceNotFoundException("ReservationAmenity", id);
        }
        reservationAmenityRepository.deleteById(id);
    }

    /** Falls back to the amenity price when none is given, then works out the subtotal. */
    private void calculateAmounts(ReservationAmenity line, Amenity amenity) {
        if (line.getQuantity() == null || line.getQuantity() < 1) {
            throw new BusinessRuleException("The quantity must be at least 1.");
        }
        if (line.getUnitPrice() == null) {
            line.setUnitPrice(amenity.getPrice());
        }
        line.setSubtotal(line.getUnitPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
    }

    private Reservation resolveReservation(Long reservationId) {
        if (reservationId == null) {
            throw new BusinessRuleException("The line requires a reservationId.");
        }
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));
    }

    private Amenity resolveAmenity(Long amenityId) {
        if (amenityId == null) {
            throw new BusinessRuleException("The line requires an amenityId.");
        }
        return amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity", amenityId));
    }
}
