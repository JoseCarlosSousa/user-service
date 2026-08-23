package pt.kkosmico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO {
	private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
