package pt.kkosmico.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    @Email
    @NotBlank
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Allows receiving password on POST, but hides it on GET/POST responses
    private String password;
    private String role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = java.time.LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private boolean active = true;
}
