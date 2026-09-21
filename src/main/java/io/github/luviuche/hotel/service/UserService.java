package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.Role;
import io.github.luviuche.hotel.entity.User;
import io.github.luviuche.hotel.exception.BusinessRuleException;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.RoleRepository;
import io.github.luviuche.hotel.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessRuleException("A user with email '" + user.getEmail() + "' already exists.");
        }
        if (userRepository.existsByDocumentNumber(user.getDocumentNumber())) {
            throw new BusinessRuleException(
                    "A user with document number '" + user.getDocumentNumber() + "' already exists.");
        }
        user.setRole(resolveRole(user.getRoleId()));
        user.setId(null);
        return userRepository.save(user);
    }

    public User update(Long id, User data) {
        User existing = findById(id);
        if (!existing.getEmail().equals(data.getEmail())
                && userRepository.existsByEmail(data.getEmail())) {
            throw new BusinessRuleException("A user with email '" + data.getEmail() + "' already exists.");
        }
        if (!existing.getDocumentNumber().equals(data.getDocumentNumber())
                && userRepository.existsByDocumentNumber(data.getDocumentNumber())) {
            throw new BusinessRuleException(
                    "A user with document number '" + data.getDocumentNumber() + "' already exists.");
        }
        existing.setRole(resolveRole(data.getRoleId()));
        existing.setName(data.getName());
        existing.setLastName(data.getLastName());
        existing.setEmail(data.getEmail());
        existing.setPhone(data.getPhone());
        existing.setDocumentNumber(data.getDocumentNumber());
        if (data.getPassword() != null && !data.getPassword().isBlank()) {
            existing.setPassword(data.getPassword());
        }
        return userRepository.save(existing);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }

    private Role resolveRole(Long roleId) {
        if (roleId == null) {
            throw new BusinessRuleException("A user requires a roleId.");
        }
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleId));
    }
}
