package io.github.luviuche.hotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_room")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @JsonProperty("reservationId")
    public Long getReservationId() {
        return reservation != null ? reservation.getId() : null;
    }

    public void setReservationId(Long reservationId) {
        if (reservationId == null) {
            this.reservation = null;
        } else {
            Reservation reference = new Reservation();
            reference.setId(reservationId);
            this.reservation = reference;
        }
    }

    @JsonProperty("roomId")
    public Long getRoomId() {
        return room != null ? room.getId() : null;
    }

    public void setRoomId(Long roomId) {
        if (roomId == null) {
            this.room = null;
        } else {
            Room reference = new Room();
            reference.setId(roomId);
            this.room = reference;
        }
    }
}
