package ssdd.practicaWeb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssdd.practicaWeb.entities.GymUser;

import java.util.Optional;


public interface UserRepository extends JpaRepository<GymUser, Long> {
    Optional<GymUser> findByUsername(String username);
}
