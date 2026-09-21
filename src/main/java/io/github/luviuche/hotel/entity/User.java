package io.github.luviuche.hotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The Role object is hidden from the JSON payload; clients work with "roleId".
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    // Accepted on create and update, never returned in a response.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Exposes the foreign key as a plain id in the JSON payload (both ways). */
    @JsonProperty("roleId")
    public Long getRoleId() {
        return role != null ? role.getId() : null;
    }

    public void setRoleId(Long roleId) {
        if (roleId == null) {
            this.role = null;
        } else {
            Role reference = new Role();
            reference.setId(roleId);
            this.role = reference;
        }
    }
}
