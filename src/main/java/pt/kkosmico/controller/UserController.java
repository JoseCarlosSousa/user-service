package pt.kkosmico.controller;

import org.springframework.http.MediaType;
import pt.kkosmico.dto.LoginRequestDTO;
import pt.kkosmico.dto.LoginResponseDTO;
import pt.kkosmico.dto.RegisterDTO;
import pt.kkosmico.model.User;
import pt.kkosmico.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping(value = "/register",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE}
    )
    public ResponseEntity<RegisterDTO> registerUser(@RequestBody RegisterDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    /**
     * Endpoint to authenticate an existing user.
     */
    @PostMapping(value = "/login",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE},
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE}
    )
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
