package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByDocumentNumber(String documentNumber);

    // Underscore: forces the role.id traversal (avoids clashing with getRoleId()).
    List<User> findByRole_Id(Long roleId);
}
