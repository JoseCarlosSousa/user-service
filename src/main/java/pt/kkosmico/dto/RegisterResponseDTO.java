package pt.kkosmico.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO {
	private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
