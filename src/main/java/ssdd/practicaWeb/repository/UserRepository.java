package ssdd.practicaWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ssdd.practicaWeb.model.Nutrition;
import ssdd.practicaWeb.model.Training;
import ssdd.practicaWeb.model.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    @SuppressWarnings("null")
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    boolean existsByEmail(String email);
    List<User> findByTrainingListContaining(Training training);
    List<User> findByNutritionListContaining(Nutrition nutrition);
}
