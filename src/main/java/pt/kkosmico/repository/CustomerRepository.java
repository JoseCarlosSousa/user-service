package pt.kkosmico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.kkosmico.model.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByUserId(String userId);
}
