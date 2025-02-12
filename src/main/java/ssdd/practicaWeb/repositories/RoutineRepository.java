package ssdd.practicaWeb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.entities.Routine;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Optional<List<Routine>> findByGymUser(GymUser gymUser);
}
