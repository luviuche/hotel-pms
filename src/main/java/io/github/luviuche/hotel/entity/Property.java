package io.github.luviuche.hotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.luviuche.hotel.enums.ActivationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "property")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_chain_id", nullable = false)
    private HotelChain hotelChain;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ActivationStatus status;

    /** Foreign key to the chain, exposed as a plain id in the JSON payload. */
    @JsonProperty("hotelChainId")
    public Long getHotelChainId() {
        return hotelChain != null ? hotelChain.getId() : null;
    }

    public void setHotelChainId(Long hotelChainId) {
        if (hotelChainId == null) {
            this.hotelChain = null;
        } else {
            HotelChain reference = new HotelChain();
            reference.setId(hotelChainId);
            this.hotelChain = reference;
        }
    }
}
