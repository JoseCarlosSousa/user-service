package pt.kkosmico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
