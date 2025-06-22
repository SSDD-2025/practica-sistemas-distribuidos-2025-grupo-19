package ssdd.practicaWeb.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ssdd.practicaWeb.model.Food;
import ssdd.practicaWeb.model.Nutrition;
import ssdd.practicaWeb.model.User;
import ssdd.practicaWeb.service.FoodService;
import ssdd.practicaWeb.service.NutritionService;
import ssdd.practicaWeb.service.UserService;

import java.security.Principal;
import java.util.Optional;


@Controller
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private NutritionService nutritionService;

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                if (user.get().isRole("USER")) {
                    model.addAttribute("user", true);
                }
                if (user.get().isRole("ADMIN")) {
                    model.addAttribute("admin", true);
                }
            }
            model.addAttribute("logged", true);
            model.addAttribute("userName", principal.getName());
        } else {
            model.addAttribute("logged", false);
        }
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());
    }

    @GetMapping("/listFoods/{nutritionId}")
    public String interfaceListFood(@PathVariable Long nutritionId, Model model) {
        model.addAttribute("foods", foodService.getAllFood());
        Nutrition nutrition = nutritionService.getNutrition(nutritionId);
        if (nutrition != null) {
            model.addAttribute("nutrition", nutrition);
            return "foodSearch";
        }
        return "redirect:/nutritions";
    }

    @GetMapping("/listFoods/deleteFoodList/{nutritionId}/{foodId}")
    public String deleteFoodList(@PathVariable Long nutritionId, @PathVariable Long foodId) {
        Nutrition nutrition = nutritionService.getNutrition(nutritionId);
        Food food = foodService.getFood(foodId);
        nutritionService.deleteListFood(nutrition,food);
        return "redirect:/listFoods/" + nutritionId;
    }

    @GetMapping("/listFoods/addFood/{nutritionId}/{foodId}")
    public String addFood(@PathVariable Long nutritionId, @PathVariable Long foodId) {
        Nutrition nutrition = nutritionService.getNutrition(nutritionId);
        Food food = foodService.getFood(foodId);
        nutritionService.addFood(nutrition,food);
        return "redirect:/listFoods/" + nutritionId;
    }


    @GetMapping("/listFoods/createFood/{nutritionId}")
    public String InterfaceCreateFood(@PathVariable Long nutritionId, Model model) {
        model.addAttribute("food",new Food());
        model.addAttribute("nutritionId", nutritionId);
        return "createFood";
    }
    @PostMapping("/listFoods/createFood/{nutritionId}")
    public String addFood(@ModelAttribute Food food, @PathVariable Long nutritionId){
        foodService.createFood(food);
        return "redirect:/listFoods/" + nutritionId;
    }

    @GetMapping("/listFoods/detailsFood/{foodId}")
    public String detailsFood(@PathVariable Long foodId, Model model, @RequestParam("nutritionId") Long nutritionId){
        Food food = foodService.getFood(foodId);
        if (food == null) {
            return "redirect:/listFoods/" + nutritionId;
        }
        model.addAttribute("food", food);
        model.addAttribute("nutritionId", nutritionId);
        return "detailsFood";
    }

    @GetMapping("/listFoods/editFood/{foodId}")
    public String showFormEdit(@PathVariable Long foodId, Model model, @RequestParam("nutritionId") Long nutritionId) {
        Food food = foodService.getFood(foodId);
        if(food != null){
            model.addAttribute("food", food);
        }
        model.addAttribute("nutritionId",nutritionId);
        return "editFood";
    }
    @PostMapping("/listFoods/editFood/{foodId}")
    public String editFood(@ModelAttribute Food food, @PathVariable Long foodId, @RequestParam("nutritionId") Long nutritionId) {
        foodService.updateFood(foodId, food);
        return "redirect:/listFoods/" + nutritionId;
    }
    @GetMapping("/listFoods/deleteFood/{foodId}")
    public String deleteFood(@PathVariable Long foodId, @RequestParam("nutritionId") Long nutritionId) {
        foodService.deleteFood(foodId);
        return "redirect:/listFoods/" + nutritionId;
    }

}
