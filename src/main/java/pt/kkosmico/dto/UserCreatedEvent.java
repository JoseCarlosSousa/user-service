package pt.kkosmico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    
	private UUID id;
    private String firstName;
    private String lastName;
    private String email;
}
