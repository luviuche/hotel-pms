package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.Role;
import io.github.luviuche.hotel.exception.BusinessRuleException;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
    }

    public Role create(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new BusinessRuleException("A role named '" + role.getName() + "' already exists.");
        }
        role.setId(null);
        return roleRepository.save(role);
    }

    public Role update(Long id, Role data) {
        Role existing = findById(id);
        if (!existing.getName().equals(data.getName())
                && roleRepository.existsByName(data.getName())) {
            throw new BusinessRuleException("A role named '" + data.getName() + "' already exists.");
        }
        existing.setName(data.getName());
        existing.setDescription(data.getDescription());
        return roleRepository.save(existing);
    }

    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role", id);
        }
        roleRepository.deleteById(id);
    }
}
