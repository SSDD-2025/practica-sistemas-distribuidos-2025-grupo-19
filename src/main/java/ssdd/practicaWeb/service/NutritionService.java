package ssdd.practicaWeb.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssdd.practicaWeb.dto.NutritionDTO;
import ssdd.practicaWeb.dto.NutritionMapper;
import ssdd.practicaWeb.model.Food;
import ssdd.practicaWeb.model.User;
import ssdd.practicaWeb.model.Nutrition;
import ssdd.practicaWeb.repository.FoodRepository;
import ssdd.practicaWeb.repository.NutritionRepository;
import ssdd.practicaWeb.repository.UserRepository;

import java.util.*;

@Service
public class NutritionService {

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NutritionMapper nutritionMapper;

    public Nutrition createNutrition(Nutrition nutrition, User user) {
        Nutrition nutrition1 = new Nutrition(nutrition.getName(),nutrition.getType(),new ArrayList<>());
        if(nutrition.getListFoods() != null){
            for(Food food: nutrition.getListFoods()){
                Optional<Food> foodAUX = foodRepository.findByName(food.getName());
                if(foodAUX.isPresent()){
                    addFood(nutrition1,foodAUX.get());
                }else{
                    return null;
                }
            }
        }
        nutrition1.setUser(user);
        nutritionRepository.save(nutrition1);
        return nutrition1;
    }

    public Nutrition getNutrition(Long id) {
        Optional<Nutrition> theNutrition = nutritionRepository.findById(id);
        if (theNutrition.isPresent()) {
            Nutrition nutrition = theNutrition.get();
            return nutrition;
        } else {
            return null;
        }
    }

    public List<Nutrition> getAllNutritions() {
        List<Nutrition> listNutrition = nutritionRepository.findAll();
        return listNutrition.isEmpty() ? null : listNutrition;
    }


    public Nutrition updateNutrition(Long nutritionId, Nutrition nutrition) {
        Optional<Nutrition> theNutrition = nutritionRepository.findById(nutritionId);

        if (theNutrition.isPresent()) {
            Nutrition existingNutrition = theNutrition.get();

            existingNutrition.setUser(nutrition.getUser());
            existingNutrition.setName(nutrition.getName());
            existingNutrition.setType(nutrition.getType());

            if(nutrition.getListFoods() != null){ //Change listFood only if user had introduced any food
                for (Food food : nutrition.getListFoods()) {
                    if (foodService.getFood(food.getName()) == null) {
                        return null;  // If any food doesn`t exit, we cancel the operation
                    }
                }

                existingNutrition.setListFoods(nutrition.getListFoods());

            }

            nutritionRepository.save(existingNutrition);
            return existingNutrition;
        }

        return null;
    }


    private void resetListFood(Nutrition nutrition){
        if(nutrition.getListFoods() != null){
            List<Food> list = new ArrayList<>(nutrition.getListFoods());
            for (Food food : list) {
                deleteListFood(nutrition, food);
            }
            nutrition.getListFoods().clear();
        }
    }
    public Nutrition deleteNutrition(Long id) {

        Optional<Nutrition> theNutrition = nutritionRepository.findById(id);

        if (theNutrition.isPresent()) {
            Nutrition nutrition = theNutrition.get();

            Collection<Food> foods = foodService.getAllFood();
            Iterator<Food> iterator = foods.iterator();
            while ((iterator.hasNext())){
                Food f = iterator.next();
                if(f.getListNutritions().contains(nutrition)) {
                    f.getListNutritions().remove(nutrition);
                }
            }
            List<User> usersWithNutrition = userRepository.findByNutritionListContaining(nutrition);
            for (User user : usersWithNutrition) {
                user.getNutritionList().remove(nutrition);
                userRepository.save(user);
            }
            nutritionRepository.delete(nutrition);
            return nutrition;
        }
        return null;
    }

    public void addFood(Nutrition nutrition, Food food) {
        if (!nutrition.getListFoods().contains(food)) {
            nutrition.getListFoods().add(food);
        }

        if (!food.getListNutritions().contains(nutrition)) {
            food.getListNutritions().add(nutrition);
        }

        nutritionRepository.save(nutrition);
        foodRepository.save(food);
    }

    public void deleteListFood (Nutrition nutrition, Food food){

        nutrition.getListFoods().remove(food);

        Collection<Food> foods  = foodService.getAllFood();
        Iterator<Food> iterator = foods.iterator();
        while ((iterator.hasNext())){
            Food f = iterator.next();
            if(f.getListNutritions().contains(nutrition)) {
                food.getListNutritions().remove(nutrition);
            }
        }
        nutritionRepository.save(nutrition);
    }
    public List<Nutrition> getNutritionsUser(User user){
        Optional<List<Nutrition>> nutritions = nutritionRepository.findByUser(user);
        if(nutritions.isPresent()){
            return nutritions.get();
        }
        return null;
    }

    public void subscribeNutrition(Long id , User user) {
        Optional<Nutrition> nutrition = nutritionRepository.findById(id);
        if (nutrition.isPresent()) {
            user.getNutritionList().add(nutrition.get());
            userRepository.save(user);
        }
    }

    public void unsubscribeNutrition(Long id, User user) {
        Optional<Nutrition> nutrition = nutritionRepository.findById(id);
        if (nutrition.isPresent()) {
            user.getNutritionList().remove(nutrition.get());
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long nutritionId, Authentication authentication) {
        return nutritionRepository.findWithUserById(nutritionId)
                .map(nutrition -> {
                    User user = nutrition.getUser();
                    return user != null && authentication.getName().equals(user.getName());
                })
                .orElse(false);
    }

    public  User getAuthenticationUser (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null ) {
            Optional<User> user = userRepository.findByEmail(authentication.getName());
            if (user.isPresent()){
                return user.get();
            }
        }
        return null;
    }

    public Collection<NutritionDTO> getAllDtoUserNutritions() {
        User currentUser = getAuthenticationUser();
        if (currentUser != null) {
            List<Nutrition> nutritions = currentUser.getNutritionList();

            return nutritions.stream()
                    .map(nutritionMapper::toDTO)
                    .toList();
        }

        return null;
    }
    public NutritionDTO toDTO(Nutrition nutrition) {
        return nutritionMapper.toDTO(nutrition);
    }

    public Nutrition toDomain(NutritionDTO nutritionDTO) {

        //return nutritionMapper.toDomain(nutritionDTO);
        Nutrition nutrition = nutritionMapper.toDomain(nutritionDTO);

        // Cargar las entidades Food usando el repositorio
        List<Food> foods = foodRepository.findAllById(nutritionDTO.listIdsFood());
        nutrition.setListFoods(foods);

        // Asignar usuario, si es necesario (también cargado desde repo)

        return nutrition;
    }


    public Collection<NutritionDTO> toDTOs(Collection<Nutrition> nutritions) {
        return nutritionMapper.toDTOs(nutritions);
    }

    public NutritionDTO getNutritionDTO(Long id) {
        return toDTO(nutritionRepository.findById(id).orElseThrow());
    }

    public Collection<NutritionDTO> getAllNutritionsDTO() {
        return toDTOs(nutritionRepository.findAll());
    }

    public boolean subscribeNutritionDTO(Long nutritionId, String name) {
        User user = userRepository.findByEmail(name)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Nutrition nutrition = nutritionRepository.findById(nutritionId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition not found"));

        if (user.getNutritionList().contains(nutrition)) {
            return true;
        }

        user.getNutritionList().add(nutrition);
        userRepository.save(user);
        return false;
    }

    public boolean unsubscribeNutritionDTO(Long nutritionId, String name) {
        User user = userRepository.findByEmail(name)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Nutrition nutrition = nutritionRepository.findById(nutritionId)
                .orElseThrow(() -> new IllegalArgumentException("Nutrition not found"));

        if (!user.getNutritionList().contains(nutrition)) {
            return false;
        }

        user.getNutritionList().remove(nutrition);
        userRepository.save(user);
        return true;
    }
}
