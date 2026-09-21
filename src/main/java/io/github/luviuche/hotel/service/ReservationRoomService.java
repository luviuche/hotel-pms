package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.Reservation;
import io.github.luviuche.hotel.entity.ReservationRoom;
import io.github.luviuche.hotel.entity.Room;
import io.github.luviuche.hotel.exception.BusinessRuleException;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.ReservationRepository;
import io.github.luviuche.hotel.repository.ReservationRoomRepository;
import io.github.luviuche.hotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReservationRoomService {

    private final ReservationRoomRepository reservationRoomRepository;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationRoomService(ReservationRoomRepository reservationRoomRepository,
                                  ReservationRepository reservationRepository,
                                  RoomRepository roomRepository) {
        this.reservationRoomRepository = reservationRoomRepository;
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationRoom> findAll() {
        return reservationRoomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ReservationRoom findById(Long id) {
        return reservationRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReservationRoom", id));
    }

    public ReservationRoom create(ReservationRoom line) {
        line.setReservation(resolveReservation(line.getReservationId()));
        line.setRoom(resolveRoom(line.getRoomId()));
        line.setId(null);
        return reservationRoomRepository.save(line);
    }

    public ReservationRoom update(Long id, ReservationRoom data) {
        ReservationRoom existing = findById(id);
        existing.setReservation(resolveReservation(data.getReservationId()));
        existing.setRoom(resolveRoom(data.getRoomId()));
        return reservationRoomRepository.save(existing);
    }

    public void delete(Long id) {
        if (!reservationRoomRepository.existsById(id)) {
            throw new ResourceNotFoundException("ReservationRoom", id);
        }
        reservationRoomRepository.deleteById(id);
    }

    private Reservation resolveReservation(Long reservationId) {
        if (reservationId == null) {
            throw new BusinessRuleException("The line requires a reservationId.");
        }
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));
    }

    private Room resolveRoom(Long roomId) {
        if (roomId == null) {
            throw new BusinessRuleException("The line requires a roomId.");
        }
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room", roomId));
    }
}
