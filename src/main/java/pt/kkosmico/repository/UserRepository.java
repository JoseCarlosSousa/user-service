package pt.kkosmico.repository;

import pt.kkosmico.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Custom query method to find a user by email during login
    Optional<User> findByEmail(String email);
}
