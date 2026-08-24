package pt.kkosmico.service;

import java.util.List;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pt.kkosmico.config.RabbitMQConfig;
import pt.kkosmico.dto.LoginRequestDTO;
import pt.kkosmico.dto.LoginResponseDTO;
import pt.kkosmico.dto.RegisterRequestDTO;
import pt.kkosmico.dto.RegisterResponseDTO;
import pt.kkosmico.dto.UserCreatedEvent;
import pt.kkosmico.dto.UserResponseDTO;
import pt.kkosmico.model.Customer;
import pt.kkosmico.model.User;
import pt.kkosmico.repository.CustomerRepository;
import pt.kkosmico.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .toList();
    }
    
    @Override
    @Transactional
    public void updateUserRole(UUID userId, String newRole) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado com o ID: " + userId));

        user.setRole(newRole);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public RegisterResponseDTO createUser(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }

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

        UserCreatedEvent event = new UserCreatedEvent();
        event.setId(savedUser.getId());
        event.setEmail(savedUser.getEmail());
        if (savedUser.getCustomer() != null) {
            event.setFirstName(savedUser.getCustomer().getFirstName());
            event.setLastName(savedUser.getCustomer().getLastName());
        }
        sendQueue(event);

        return new RegisterResponseDTO(
        		savedUser.getId(),
                savedUser.getEmail(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName(),
                savedUser.getRole()
        );
    }
    
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = userRepository.findByEmailWithCustomer(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        Customer customer = user.getCustomer();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = tokenService.generateToken(new RegisterResponseDTO(
                user.getId(),
                user.getEmail(),
                customer.getFirstName(),
                customer.getLastName(),
                user.getRole()
        ));

        return new LoginResponseDTO(token, "Bearer");
    }

    private void sendQueue(UserCreatedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USER_EXCHANGE,
                    RabbitMQConfig.USER_CREATED_ROUTING_KEY,
                    event
            );
            System.out.println("Message successfully published to RabbitMQ!");
        } catch (Exception e) {
            System.err.println("Failed to publish message to RabbitMQ: " + e.getMessage());
        }
    }
}
