package pt.kkosmico.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates constructor with all fields to easily return the token
public class LoginResponseDTO {
    private String token;
    private String type; // Usually "Bearer"
}
