package pt.kkosmico.repository;

import pt.kkosmico.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	   
	Optional<User> findByEmail(String email);
	   
	@Query("SELECT u FROM User u " +
		       "JOIN FETCH u.customer c " +
		       "WHERE u.email = :email")
		Optional<User> findByEmailWithCustomer(@Param("email") String email);
}
