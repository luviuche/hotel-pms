package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    // Underscore: forces the hotelChain.id traversal (avoids clashing with getHotelChainId()).
    List<Property> findByHotelChain_Id(Long hotelChainId);

    List<Property> findByCity(String city);
}
