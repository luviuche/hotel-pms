package io.github.luviuche.hotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.luviuche.hotel.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(name = "room_number", nullable = false)
    private String number;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoomStatus status;

    @Column(nullable = false)
    private Boolean available;

    @JsonProperty("propertyId")
    public Long getPropertyId() {
        return property != null ? property.getId() : null;
    }

    public void setPropertyId(Long propertyId) {
        if (propertyId == null) {
            this.property = null;
        } else {
            Property reference = new Property();
            reference.setId(propertyId);
            this.property = reference;
        }
    }

    @JsonProperty("roomTypeId")
    public Long getRoomTypeId() {
        return roomType != null ? roomType.getId() : null;
    }

    public void setRoomTypeId(Long roomTypeId) {
        if (roomTypeId == null) {
            this.roomType = null;
        } else {
            RoomType reference = new RoomType();
            reference.setId(roomTypeId);
            this.roomType = reference;
        }
    }
}
