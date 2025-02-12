package ssdd.practicaWeb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssdd.practicaWeb.entities.Food;
import ssdd.practicaWeb.entities.Nutrition;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.service.FoodService;
import ssdd.practicaWeb.service.NutritionService;
import ssdd.practicaWeb.service.UserService;

@Controller
public class NutritionController {

    @Autowired
    private NutritionService nutritionService;
    @Autowired
    private UserService userService;
    @Autowired
    private FoodService foodService;


    @GetMapping("/Nutrition")
    public String InterfaceNutrition(@RequestParam("userId") Long userId, Model model) {
        GymUser user = userService.getGymUser(userId);
        if (user != null){
            model.addAttribute("userId",user.getId());
            return "preListNutrition";
        }
        return "redirect:/FrontPage";
    }
    @GetMapping("/ListNutrition")
    public String InterfaceListNutrition(@RequestParam("userId") Long userId, Model model) {
        model.addAttribute("nutrition", nutritionService.getAll(userId));
        GymUser user = userService.getGymUser(userId);
        if (user != null){
            model.addAttribute("userId",user.getId());
            return "listNutrition";
        }
        return "redirect:/Nutrition";
    }
    @GetMapping("/ListNutrition/CreateNutrition")
    public String InterfaceCreateNutrition(@RequestParam("userId") Long userId, Model model) {
        model.addAttribute("nutricion",new Nutrition());
        GymUser user = userService.getGymUser(userId);
        if (user != null){
            model.addAttribute("userId",user.getId());
            return "createNutrition";
        }
        return "redirect:/FrontPage";
    }
    @PostMapping("/ListNutrition/CreateNutrition")
    public String addNutrition(Nutrition nutrition, @RequestParam("userId") Long userId){
        GymUser user = userService.getGymUser(userId);
        if (user != null){
            nutrition.setGymUser(user);
            Nutrition nut = nutritionService.createNutrition(nutrition,user);
            return "redirect:/ListFoods?nutritionId=" + nut.getId();
        }
        return "redirect:/FrontPage";
    }

    @GetMapping("/ListNutrition/detailsNutrition/{nutritionId}")
    public String detailsNutrition(@PathVariable Long nutritionId, Model model, @RequestParam("userId") Long userId){
        Nutrition nutrition = nutritionService.getNutrition(nutritionId);
        if (nutrition == null) {
            return "redirect:/ListNutrition";
        }
        model.addAttribute("nutrition", nutrition);
        GymUser user = userService.getGymUser(userId);
        if(user != null){
            model.addAttribute("userId",user.getId());
            return "detailsNutrition";
        }
        return "redirect:/ListNutrition";
    }

    @GetMapping("/ListNutrition/ModifyNutrition/{nutritionId}")
    public String showFormEdit(@PathVariable Long nutritionId, Model model, @RequestParam("userId") Long userId) {
        Nutrition nutrition = nutritionService.getNutrition(nutritionId);
        if(nutrition != null){
            model.addAttribute("nutrition", nutrition);
        }
        model.addAttribute("userId",userId);
        return "modifyNutrition";
    }

    @PostMapping("/ListNutrition/ModifyNutrition/{nutritionId}")
    public String editNutrition(Nutrition nutrition,@PathVariable Long nutritionId,@RequestParam("userId") Long userId) {
        GymUser user = userService.getGymUser(userId);
        nutrition.setGymUser(user);
        nutritionService.updateNutrition(nutritionId, nutrition, user);
        return "redirect:/ListFoods?nutritionId=" + nutritionId;
    }
    @GetMapping("/ListNutrition/DeleteNutrition/{nutritionId}")
    public String deleteNutrition(@PathVariable Long nutritionId, @RequestParam("userId") Long userId) {
        nutritionService.deleteNutrition(nutritionId);
        return "redirect:/ListNutrition?userId=" + userId;
    }
    @GetMapping("/ListFoods/deleteFoodList")
    public String deleteFoodList(@RequestParam("nutritionId") Long nutritionId, @RequestParam("foodId") Long foodId) {
        Nutrition nutrition = nutritionService.getNutrition(nutritionId);
        Food food = foodService.getFood(foodId);
        nutritionService.deleteListFood(nutrition,food);
        return "redirect:/ListFoods?nutritionId=" + nutritionId;
    }

    @GetMapping("/ListFoods/addFood")
    public String addFood(@RequestParam("nutritionId") Long nutritionId, @RequestParam("foodId") Long foodId) {
        Nutrition nutrition = nutritionService.getNutrition(nutritionId);
        Food food = foodService.getFood(foodId);
        nutritionService.addFood(nutrition,food);
        return "redirect:/ListFoods?nutritionId=" + nutritionId;
    }

    @GetMapping("/ListFoods")
    public String InterfaceListFood(@RequestParam ("nutritionId") Long nutritionId, Model model) {
        model.addAttribute("food", foodService.getAllFood());
        Nutrition nutrition = nutritionService.getNutrition(nutritionId);
        if (nutrition != null) {
            model.addAttribute("nutrition", nutrition);
            model.addAttribute("userId", nutrition.getGymUser().getId());
            return "foodSearch";
        }
        return "redirect:/ListNutrition";
    }
}

