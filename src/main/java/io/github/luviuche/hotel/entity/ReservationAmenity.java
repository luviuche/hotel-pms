package io.github.luviuche.hotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "reservation_amenity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationAmenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private BigDecimal subtotal;

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

    @JsonProperty("amenityId")
    public Long getAmenityId() {
        return amenity != null ? amenity.getId() : null;
    }

    public void setAmenityId(Long amenityId) {
        if (amenityId == null) {
            this.amenity = null;
        } else {
            Amenity reference = new Amenity();
            reference.setId(amenityId);
            this.amenity = reference;
        }
    }
}
