package pt.kkosmico.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Implements Serializable so the object can be transformed into bytes by RabbitMQ
public class UserCreatedEventDTO implements Serializable {
    private Long id;
    private String name;
    private String email;
}
