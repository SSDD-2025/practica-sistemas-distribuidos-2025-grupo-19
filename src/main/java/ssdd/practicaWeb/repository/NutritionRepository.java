package ssdd.practicaWeb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ssdd.practicaWeb.model.User;
import ssdd.practicaWeb.model.Nutrition;

import java.util.List;
import java.util.Optional;

public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    Optional<List<Nutrition>> findByUser(User user);

    @EntityGraph(attributePaths = "user")
    Optional<Nutrition> findWithUserById(Long id);
}
