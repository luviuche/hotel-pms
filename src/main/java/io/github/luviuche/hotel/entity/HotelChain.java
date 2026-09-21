package io.github.luviuche.hotel.entity;

import io.github.luviuche.hotel.enums.ActivationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hotel_chain")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelChain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ActivationStatus status;
}
