package pt.kkosmico.userservice.dto;

import lombok.Data;

@Data // Generates getters and setters for the incoming payload
public class LoginRequestDTO {
    private String email;
    private String password;
}
