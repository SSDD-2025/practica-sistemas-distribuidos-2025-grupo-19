package ssdd.practicaWeb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ssdd.practicaWeb.model.User;
import ssdd.practicaWeb.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    Optional<List<Training>> findByUser(User user);

    @EntityGraph(attributePaths = "user")
    Optional<Training> findWithUserById(Long id);
}
