package pt.kkosmico.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import pt.kkosmico.config.RabbitMQConfig;
import pt.kkosmico.dto.LoginRequestDTO;
import pt.kkosmico.dto.LoginResponseDTO;
import pt.kkosmico.dto.RegisterDTO;
import pt.kkosmico.model.Customer;
import pt.kkosmico.model.User;
import pt.kkosmico.repository.CustomerRepository;
import pt.kkosmico.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder; // Injected BCrypt encoder bean
    private final TokenService tokenService; // Injected modern TokenService
    private final RabbitTemplate rabbitTemplate;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public RegisterDTO createUser(RegisterDTO dto) {
        // 1. Check if the email is already registered in the database
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }

        // 2. Encrypt the password if the email is free
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword("");
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(encryptedPassword);
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        Customer customer = new Customer();
        customer.setUserId(savedUser.getId());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());

        Customer sustomer = customerRepository.save(customer);


        // 3. Publish the event to RabbitMQ broker asynchronously
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USER_EXCHANGE,
                    RabbitMQConfig.USER_CREATED_ROUTING_KEY,
                    savedUser
            );
            System.out.println("Message successfully published to RabbitMQ!");
        } catch (Exception e) {
            System.err.println("Failed to publish message to RabbitMQ: " + e.getMessage());
        }
        return new RegisterDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                "",
                customer.getFirstName(),
                customer.getLastName()
        );
    }


    /**
     * Validates credentials and returns a real encrypted JWT token.
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // 1. Check if user exists by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 2. Match raw password with DB encrypted hash
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 3. Generate the real production-ready JWT token
        String realToken = tokenService.generateToken(new RegisterDTO("", user.getEmail(), "", customer.getFirstName(), customer.getLastName()));

        return new LoginResponseDTO(realToken, "Bearer");
    }
}
