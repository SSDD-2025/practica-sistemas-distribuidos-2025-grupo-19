package ssdd.practicaWeb.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.entities.Nutrition;
import ssdd.practicaWeb.entities.NutritionDTO;
import ssdd.practicaWeb.service.NutritionService;
import ssdd.practicaWeb.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/api/nutrition")
public class NutritionRESTController {

    @Autowired
    private NutritionService nutritionService;
    @Autowired
    private UserService userService;

    @PostMapping("/{userId}")
    public ResponseEntity<NutritionDTO> createNutrition(@RequestBody Nutrition nutrition, @PathVariable Long userId) {
        GymUser user = userService.getGymUser(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        Nutrition nutritionObt = nutritionService.createNutrition(nutrition,user);
        if(nutritionObt==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(201).body(new NutritionDTO(nutritionObt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutritionDTO> getNutricion(@PathVariable Long id) {
        Nutrition nutrition = nutritionService.getNutrition(id);
        if (nutrition == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new NutritionDTO(nutrition));
    }
    @GetMapping("/all/{userId}")
    public ResponseEntity<Collection<NutritionDTO>> allNutritions(@PathVariable Long userId) {
        List<NutritionDTO> list = new ArrayList<>();
        Collection<Nutrition> listNutrition = nutritionService.getAll(userId);
        for(Nutrition nutrition: listNutrition){
            list.add(new NutritionDTO(nutrition));
        }
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{userId}/{id}")
    public ResponseEntity<NutritionDTO> updateNutrition(@PathVariable Long id, @PathVariable Long userId, @RequestBody Nutrition nutrition) {
        GymUser user = userService.getGymUser(userId);
        if(nutrition.getListFoods() == null){
            nutrition.setListFoods(new ArrayList<>());
        }
        Nutrition update = nutritionService.updateNutrition(id, nutrition, user);
        if (update == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new NutritionDTO(update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNutrition(@PathVariable Long id) {
        nutritionService.deleteNutrition(id);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{userId}/{id}")
    public ResponseEntity<NutritionDTO> updateParcialNutrition(@PathVariable Long id, @PathVariable Long userId, @RequestBody Nutrition parcialNutrition) {
        GymUser user = userService.getGymUser(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        Nutrition existed = nutritionService.updateNutritionPatch(id, parcialNutrition, user);
        if (existed == null || user == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new NutritionDTO(existed));
    }
}