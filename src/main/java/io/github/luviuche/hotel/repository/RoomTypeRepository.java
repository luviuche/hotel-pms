package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    boolean existsByName(String name);
}
