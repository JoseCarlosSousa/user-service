package pt.kkosmico.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone_prefix")
    private String phonePrefix;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String gender;
    private String address;
    private String city;
    private String state;
    @Column(name = "zip_code")
    private String zipCode;
    private String country;
}