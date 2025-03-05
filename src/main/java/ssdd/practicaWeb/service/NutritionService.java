package ssdd.practicaWeb.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssdd.practicaWeb.entities.Food;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.entities.Nutrition;
import ssdd.practicaWeb.repositories.FoodRepository;
import ssdd.practicaWeb.repositories.NutritionRepository;

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
    private UserService userService;

    public Nutrition createNutrition(Nutrition nutrition, GymUser user) {
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
        nutrition1.setGymUser(user);
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

    public Collection<Nutrition> getAll(Long id) {
        Optional<List<Nutrition>> listNutritionUser = nutritionRepository.findByGymUser(userService.getGymUser(id));
        if(listNutritionUser.isPresent()){
            return listNutritionUser.get();
        }
        return null;
    }

    public Nutrition updateNutritionPatch(Long nutritionId, Nutrition nutrition, GymUser user) {
        Optional<Nutrition> theNutrition = nutritionRepository.findById(nutritionId);
        if(theNutrition.isPresent()) {
            Nutrition nutrition1 = theNutrition.get();
            nutrition.setGymUser(user);
            nutrition.setId(nutritionId);

            //
            if(nutrition.getListFoods() != null){

                for(Food food: nutrition.getListFoods()){
                    Optional<Food> foodAUX = foodRepository.findByName(food.getName());//getId()
                    Food f = foodService.getFood(food.getId());
                    if(foodAUX.isPresent()){
                        addFood(nutrition1,foodAUX.get());
                    }else{
                        food = new Food(food.getName(),food.getType(),0);
                        food.setListNutritions(new ArrayList<>());
                        food.setId(f.getId());
                        Food newFood = foodService.createFood(food);
                        addFood(nutrition1,newFood);
                    }
                }
            }
            nutrition.setListFoods(nutrition1.getListFoods());
            nutritionRepository.save(nutrition);
            //
            return nutrition;
        }
        return null;
    }

    public Nutrition updateNutrition(Long nutritionId, Nutrition nutrition, GymUser user) {
        Optional<Nutrition> theNutrition = nutritionRepository.findById(nutritionId);

        if (theNutrition.isPresent()) {
            Nutrition existingNutrition = theNutrition.get();

            existingNutrition.setGymUser(user);
            existingNutrition.setName(nutrition.getName());
            existingNutrition.setType(nutrition.getType());

            // keep the previews foods list
            List<Food> existingFoods = existingNutrition.getListFoods();

            for (Food food : existingFoods) {
                if (foodService.getFood(food.getName()) == null) {
                    return null;  // If any food doesn`t exit, we cancel the operation
                }
            }

            // Agregar solo nuevas foods sin duplicar
            for (Food food : existingFoods) {
                Food f = foodService.getFood(food.getName());
                if (!existingNutrition.getListFoods().contains(f)) {
                    addFood(existingNutrition, f);
                }
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
            GymUser user = nutrition.getGymUser();
            user.getListNutrition().remove(nutrition);
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
    public List<Nutrition> getNutritionsUser(GymUser user){
        Optional<List<Nutrition>> nutritions = nutritionRepository.findByGymUser(user);
        if(nutritions.isPresent()){
            return nutritions.get();
        }
        return null;
    }
}
