package ssdd.practicaWeb.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ssdd.practicaWeb.entities.Food;
import ssdd.practicaWeb.entities.FoodDTO;
import ssdd.practicaWeb.service.FoodService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/api/food")
public class FoodRESTController {

    @Autowired
    private FoodService foodService;

    @PostMapping
    public ResponseEntity<FoodDTO> createFood(@RequestBody Food food) {
        Food foodObt = foodService.createFood(food);
        return ResponseEntity.status(201).body(new FoodDTO(foodObt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDTO> getFood(@PathVariable Long id) {
        Food food = foodService.getFood(id);
        if (food == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new FoodDTO(food));
    }

    @GetMapping
    public ResponseEntity<Collection<FoodDTO>> allFoods() {
        List<FoodDTO> list = new ArrayList<>();
        List<Food> listFood = (List<Food>) foodService.getAllFood();
        for(Food food: listFood){
            list.add(new FoodDTO(food));
        }
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodDTO> updateFood(@PathVariable Long id, @RequestBody Food food) {
        Food update = foodService.updateFood(id, food);
        if (update == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new FoodDTO(update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<FoodDTO> updateParcialFood(@PathVariable Long id, @RequestBody Food food) {
        Food existed = foodService.getFood(id);
        if (existed == null){
            return ResponseEntity.notFound().build();
        }
        if (food.getName() != null) {
            existed.setName(food.getName());
        }
        if (food.getType() != null) {
            existed.setType(food.getType());
        }
        if (food.getCalories() != 0) {
            existed.setCalories(food.getCalories());
        }
        foodService.updateFood(id, existed);
        return ResponseEntity.ok(new FoodDTO(existed));
    }
}
