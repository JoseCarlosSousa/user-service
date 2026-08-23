package pt.kkosmico.service;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pt.kkosmico.config.RabbitMQConfig;
import pt.kkosmico.dto.LoginRequestDTO;
import pt.kkosmico.dto.LoginResponseDTO;
import pt.kkosmico.dto.RegisterRequestDTO;
import pt.kkosmico.dto.RegisterResponseDTO;
import pt.kkosmico.model.Customer;
import pt.kkosmico.model.User;
import pt.kkosmico.repository.CustomerRepository;
import pt.kkosmico.repository.UserRepository;

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

    public RegisterResponseDTO createUser(RegisterRequestDTO dto) {
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
        customer.setUser(user); 
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());

        Customer savedCustomer = customerRepository.save(customer);


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
        return new RegisterResponseDTO(
        		savedUser.getId(),
                savedUser.getEmail(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName()
        );
    }

    
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {

        User user = userRepository.findByEmailWithCustomer(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        Customer customer = user.getCustomer();

        // 1. Match password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 2. Generate JWT
        String token = tokenService.generateToken(new RegisterResponseDTO(
                user.getId(),
                user.getEmail(),
                customer.getFirstName(),
                customer.getLastName()
        ));

        return new LoginResponseDTO(token, "Bearer");
    }
    
}
