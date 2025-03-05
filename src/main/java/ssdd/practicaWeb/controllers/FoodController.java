package ssdd.practicaWeb.controllers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssdd.practicaWeb.entities.Food;
import ssdd.practicaWeb.repositories.FoodRepository;
import ssdd.practicaWeb.service.FoodService;
import ssdd.practicaWeb.service.UserService;


@Controller
public class FoodController {

    @Autowired
    private FoodService foodService;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/ListFoods/CreateFood")
    public String InterfaceCreateFood(@RequestParam ("nutritionId") Long nutritionId, Model model) {
        model.addAttribute("food",new Food());
        model.addAttribute("nutritionId", nutritionId);
        return "createFood";
    }
    @PostMapping("/ListFoods/CreateFood")
    public String addFood(Food food, @RequestParam("nutritionId") Long nutritionId){
        foodService.createFood(food);
        return "redirect:/ListFoods?nutritionId=" + nutritionId;
    }

    @GetMapping("/ListFoods/detailsFood/{foodId}")
    public String detailsFood(@PathVariable Long foodId, Model model, @RequestParam("nutritionId") Long nutritionId){
        Food food = foodService.getFood(foodId);
        if (food == null) {
            return "redirect:/ListFoods";
        }
        model.addAttribute("food", food);
        model.addAttribute("nutritionId", nutritionId);
        return "detailsFood";
    }

    @GetMapping("/ListFoods/ModifyFood/{foodId}")
    public String showFormEdit(@PathVariable Long foodId, Model model, @RequestParam("nutritionId") Long nutritionId) {
        Food food = foodService.getFood(foodId);
        if(food != null){
            model.addAttribute("food", food);
        }
        model.addAttribute("nutritionId",nutritionId);
        return "modifyFood";
    }
    @PostMapping("/ListFoods/ModifyFood/{foodId}")
    public String editFood(Food food,@PathVariable Long foodId,@RequestParam("nutritionId") Long nutritionId) {
        foodService.updateFood(foodId, food);
        return "redirect:/ListFoods?nutritionId=" + nutritionId;
    }
    @GetMapping("/ListFoods/DeleteFood/{foodId}")
    public String deleteFood(@PathVariable Long foodId, @RequestParam("nutritionId") Long nutritionId) {
        foodService.deleteFood(foodId);
        return "redirect:/ListFoods?nutritionId=" + nutritionId;
    }

}
