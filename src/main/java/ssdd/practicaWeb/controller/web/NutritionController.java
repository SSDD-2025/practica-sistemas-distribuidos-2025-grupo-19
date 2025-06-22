package ssdd.practicaWeb.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class NutritionController {

    @Autowired
    private NutritionService nutritionService;
    @Autowired
    private UserService userService;
    @Autowired
    private FoodService foodService;

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

    @GetMapping("/nutritions")
    public String showAllNutritions(Model model) {
        model.addAttribute("nutritions", nutritionService.getAllNutritions());
        return "preListNutrition";
    }

    @GetMapping("/myNutritions")
    public String showMyNutritions(Model model, Principal principal) {
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent() && !user.get().isRole("ADMIN")) {
                model.addAttribute("nutritions", user.get().getNutritionList());
                return "listNutrition";
            } else{
                return "redirect:/nutritions";
            }
        }else{
            return "redirect:/login";
        }
    }
    @GetMapping("/nutritions/CreateNutrition")
    public String createNutrition(Model model, Principal principal) {
        if(principal != null){
            model.addAttribute("nutrition",new Nutrition());
            return "createNutrition";
        }
        return "redirect:/login";
    }
    @PostMapping("/nutritions/CreateNutrition")
    public String addNutrition(@Valid Nutrition nutrition, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute( "mistake", "Por favor, corrige los errores en el formulario.");
            model.addAttribute("nutrition",new Nutrition());
            return "createNutrition";
        }
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                if (user.get().isRole("ADMIN")) {
                    nutritionService.createNutrition(nutrition, null);
                } else {
                    nutritionService.createNutrition(nutrition, user.get());
                }
                return "redirect:/nutritions";
            }
        }
        return "redirect:/nutritions";
    }

    @GetMapping("/nutritions/{nutritionId}")
    public String detailsNutrition(@PathVariable Long nutritionId, Model model, Principal principal) {
        Nutrition nutrition = nutritionService.getNutrition(nutritionId);
        if (nutrition == null) {
            return "redirect:/nutritions";
        }
        model.addAttribute("nutrition", nutrition);

        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                Boolean isAdmin = user.get().isRole("ADMIN");

                Boolean canEdit = isAdmin
                        || (nutrition.getUser() != null && nutrition.getUser().getId().equals(user.get().getId()));
                Boolean isSubscribed = user.get().getNutritionList().contains(nutrition);

                model.addAttribute("subscribed", isSubscribed);
                model.addAttribute("logged", true);
                model.addAttribute("admin", user.get().isRole("ADMIN")); // Add the variable "admin"
                model.addAttribute("canEdit", canEdit);
            }
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("admin", false); // If not authenticated, the user is not an admin
        }

        return "detailsNutrition";

    }

    @GetMapping("/nutritions/editNutrition/{nutritionId}")
    public String showFormEdit(@PathVariable Long nutritionId, Model model, Principal principal) {
        Nutrition nutrition = nutritionService.getNutrition(nutritionId);
        if(nutrition == null){
            return "redirect:/nutritions";
        }
        model.addAttribute("nutrition", nutrition);
        return "editNutrition";
    }

    @PostMapping("/nutritions/editNutrition/{nutritionId}")
    public String editNutrition(@Valid Nutrition nutrition, BindingResult result, Model model,@PathVariable Long nutritionId,Principal principal) {
        if (result.hasErrors()) {
            Nutrition nutrition1 = nutritionService.getNutrition(nutritionId);
            model.addAttribute("nutrition", nutrition1);
            model.addAttribute( "mistake", "Por favor, corrige los errores en el formulario.");
            return "editNutrition";
        } else{
            if (principal != null) {
                Optional<User> user = userService.findByEmail(principal.getName());
                Nutrition optionalNutrition = nutritionService.getNutrition(nutritionId);
                if (optionalNutrition!= null && user.isPresent()) {
                    nutritionService.updateNutrition(nutritionId, nutrition);
                    return "redirect:/nutritions/" + nutritionId;
                }
            }

        }
        return "redirect:/nutritions";
    }

    @GetMapping("/nutritions/delete/{nutritionId}")
    public String deleteNutrition(@PathVariable Long nutritionId) {
        nutritionService.deleteNutrition(nutritionId);
        return "redirect:/nutritions";
    }



    @GetMapping("/nutritions/subscribe/{id}")
    public String subscribeToDiet(@PathVariable Long id, Principal principal) {
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                nutritionService.subscribeNutrition(id, user.get());
            }
        }
        return "redirect:/nutritions/" + id;
    }

    @GetMapping("/nutritions/unsubscribe/{id}")
    public String unsubscribeFromDiet(@PathVariable Long id, Principal principal) {
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                nutritionService.unsubscribeNutrition(id, user.get());
            }
        }
        return "redirect:/nutritions/" + id;
    }
}

