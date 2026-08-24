package pt.kkosmico.service;

import java.util.List;
import java.util.UUID;
import pt.kkosmico.dto.LoginRequestDTO;
import pt.kkosmico.dto.LoginResponseDTO;
import pt.kkosmico.dto.RegisterRequestDTO;
import pt.kkosmico.dto.RegisterResponseDTO;
import pt.kkosmico.dto.UserResponseDTO;

public interface UserService {
    List<UserResponseDTO> findAllUsers();
    void updateUserRole(UUID userId, String newRole);
    RegisterResponseDTO createUser(RegisterRequestDTO dto);
    LoginResponseDTO login(LoginRequestDTO loginRequest);
}
