package pt.kkosmico.controller;

import org.springframework.http.MediaType;
import pt.kkosmico.dto.LoginRequestDTO;
import pt.kkosmico.dto.LoginResponseDTO;
import pt.kkosmico.dto.RegisterRequestDTO;
import pt.kkosmico.dto.RegisterResponseDTO;
import pt.kkosmico.dto.UserResponseDTO;
import pt.kkosmico.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(
    	    produces = {
    	        MediaType.APPLICATION_JSON_VALUE,
    	        MediaType.APPLICATION_XML_VALUE,
    	        MediaType.APPLICATION_YAML_VALUE
    	    }
    	)
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
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
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    /**
     * EndPoint to authenticate an existing user.
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
