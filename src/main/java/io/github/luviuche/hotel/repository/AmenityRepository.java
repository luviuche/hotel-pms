package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.Amenity;
import io.github.luviuche.hotel.enums.AmenityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    List<Amenity> findByActiveTrue();

    List<Amenity> findByType(AmenityType type);
}
