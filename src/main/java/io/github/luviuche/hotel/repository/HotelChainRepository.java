package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.HotelChain;
import io.github.luviuche.hotel.enums.ActivationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelChainRepository extends JpaRepository<HotelChain, Long> {

    List<HotelChain> findByStatus(ActivationStatus status);
}
