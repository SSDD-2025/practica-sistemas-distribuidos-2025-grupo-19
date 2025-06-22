package ssdd.practicaWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssdd.practicaWeb.dto.FoodDTO;
import ssdd.practicaWeb.dto.FoodMapper;
import ssdd.practicaWeb.model.Food;
import ssdd.practicaWeb.repository.FoodRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class FoodService {
    @Autowired
    FoodRepository foodRepository;

    @Autowired
    FoodMapper foodMapper;



    public Food createFood(Food food){
        foodRepository.save(food);
        return food;
    }
    public Food getFood(String name){
        Optional<Food> theFood = foodRepository.findByName(name);
        if (theFood.isPresent()) {
            Food food = theFood.get();
            return food;
        } else {
            return null;
        }
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

    public Collection<FoodDTO> getAllDtoFoods(){
        return foodMapper.toDTOs(foodRepository.findAll());
    }

    public FoodDTO getDtoFood(long foodId){
        return foodMapper.toDTO(foodRepository.findById(foodId).get());
    }

    public FoodDTO toDTO(Food food) {
        return foodMapper.toDTO(food);
    }

    public Food toDomain(FoodDTO foodDTO) {
        return foodMapper.toDomain(foodDTO);
    }

    private Collection<FoodDTO> toDTOs(Collection<Food> foods) {
        return foodMapper.toDTOs(foods);
    }
}

