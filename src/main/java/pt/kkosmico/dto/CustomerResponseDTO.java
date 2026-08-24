package pt.kkosmico.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import pt.kkosmico.model.Customer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
	private UUID id;
    private String firstName;
    private String lastName;
    private String phonePrefix;
    private String phoneNumber;
    private String gender;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;


    public CustomerResponseDTO(Customer customer) {
        if (customer != null) {
            this.id = customer.getId();
            this.firstName = customer.getFirstName();
            this.lastName = customer.getLastName();
            this.phonePrefix = customer.getPhonePrefix();
            this.phoneNumber = customer.getPhoneNumber();
			this.gender = customer.getGender();
			this.address = customer.getAddress();
			this.city = customer.getCity();
			this.state = customer.getState();
			this.zipCode = customer.getZipCode();
			this.country = customer.getCountry();
        }
    }
}
