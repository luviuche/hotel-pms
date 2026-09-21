package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.Payment;
import io.github.luviuche.hotel.entity.Reservation;
import io.github.luviuche.hotel.enums.PaymentStatus;
import io.github.luviuche.hotel.exception.BusinessRuleException;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.PaymentRepository;
import io.github.luviuche.hotel.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public PaymentService(PaymentRepository paymentRepository, ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", id));
    }

    public Payment create(Payment payment) {
        Reservation reservation = resolveReservation(payment.getReservationId());
        if (paymentRepository.existsByReservation_Id(reservation.getId())) {
            throw new BusinessRuleException(
                    "Reservation " + reservation.getId() + " already has a payment on record.");
        }
        payment.setReservation(reservation);
        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.PENDING);
        }
        if (payment.getPaidAt() == null) {
            payment.setPaidAt(LocalDateTime.now());
        }
        payment.setId(null);
        return paymentRepository.save(payment);
    }

    public Payment update(Long id, Payment data) {
        Payment existing = findById(id);
        Reservation reservation = resolveReservation(data.getReservationId());
        if (!existing.getReservation().getId().equals(reservation.getId())
                && paymentRepository.existsByReservation_Id(reservation.getId())) {
            throw new BusinessRuleException(
                    "Reservation " + reservation.getId() + " already has a payment on record.");
        }
        existing.setReservation(reservation);
        existing.setAmount(data.getAmount());
        existing.setPaymentMethod(data.getPaymentMethod());
        existing.setStatus(data.getStatus() != null ? data.getStatus() : existing.getStatus());
        if (data.getPaidAt() != null) {
            existing.setPaidAt(data.getPaidAt());
        }
        return paymentRepository.save(existing);
    }

    public void delete(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment", id);
        }
        paymentRepository.deleteById(id);
    }

    private Reservation resolveReservation(Long reservationId) {
        if (reservationId == null) {
            throw new BusinessRuleException("A payment requires a reservationId.");
        }
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));
    }
}
