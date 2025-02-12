package ssdd.practicaWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssdd.practicaWeb.entities.Food;
import ssdd.practicaWeb.repositories.FoodRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class FoodService {
    @Autowired
    FoodRepository foodRepository;

    public Food createFood(Food food){
        foodRepository.save(food);
        return food;
    }
    public Food getFood(Long id){
        Optional<Food> theFood = foodRepository.findById(id);
        if (theFood.isPresent()) {
            Food food = theFood.get();
            return food;
        } else {
            return null;
        }
    }
    public Collection <Food> getAllFood(){
        return foodRepository.findAll();
    }
    public Food updateFood(Long id, Food food){
        Optional<Food> theFood = foodRepository.findById(id);
        if(theFood.isPresent()) {
            food.setId(id);
            foodRepository.save(food);
            return food;
        }

        return null;
    }
    public Food deleteFood(Long id){
        Optional<Food> theFood = foodRepository.findById(id);
        if (theFood.isPresent()) {
            Food food = theFood.get();
            foodRepository.delete(food);
            return food;
        }
        return null;
    }
}

