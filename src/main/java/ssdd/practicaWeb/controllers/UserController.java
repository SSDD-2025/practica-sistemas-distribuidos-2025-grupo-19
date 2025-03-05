package ssdd.practicaWeb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ssdd.practicaWeb.dtosEdit.CaloricPhase;
import ssdd.practicaWeb.dtosEdit.Gender;
import ssdd.practicaWeb.dtosEdit.Morphology;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.repositories.UserRepository;
import ssdd.practicaWeb.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    /*Due to security reasons, it will not be possible to view the information of all
     * existing users. A user will only be able to see details corresponding to
     * their own account*/
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    /* Para forzar un error 500
    @GetMapping("/force-error")
    public String forceError() {
        throw new RuntimeException("Error 500 forzado");
    }

     */
    @GetMapping("/users/{userId}")
    public String showProfile(Model model, @PathVariable Long userId){
        GymUser user = userService.getGymUser((userId));
        if(user != null){
            model.addAttribute("user",user);
            return "users";
        }
        return "redirect:/FrontPage";
    }

    @GetMapping("/users/edit/{userId}")
    public String editeProfileGet(Model model,@PathVariable Long userId){
        GymUser user = userService.getGymUser((userId));
        String originalGender = user.getGender();
        String originalMorphology = user.getMorphology();
        String originalCaloricPhase = user.getCaloricPhase();

        if(user != null){
            List<Gender> genders = new ArrayList<>();
            genders.add(new Gender("Masculino", "Masculino".equals(originalGender)));
            genders.add(new Gender("Femenino", "Femenino".equals(originalGender)));

            List<Morphology> morphologys = new ArrayList<>();
            morphologys.add(new Morphology("Ectomorfo", "Ectomorfo".equals(originalMorphology)));
            morphologys.add(new Morphology("Endomorfo", "Endomorfo".equals(originalMorphology)));
            morphologys.add(new Morphology("Mesomorfo", "Mesomorfo".equals(originalMorphology)));

            List<CaloricPhase> caloricPhases = new ArrayList<>();
            caloricPhases.add(new CaloricPhase("Definicion", "Definicion".equals(originalCaloricPhase)));
            caloricPhases.add(new CaloricPhase("Mantenimiento", "Mantenimiento".equals(originalCaloricPhase)));
            caloricPhases.add(new CaloricPhase("Volumen", "Volumen".equals(originalCaloricPhase)));

            model.addAttribute("genders",genders);
            model.addAttribute("morphologys",morphologys);
            model.addAttribute("caloricPhases",caloricPhases);
            model.addAttribute("user",user);
            return "edit";
        }
        return "redirect:/FrontPage";
    }

    @PostMapping("/users/edit/{userId}")
    public String editProfilePost(GymUser user, @PathVariable Long userId){
        userService.updateGymUser(userId, user);
        return "redirect:/users/{userId}";
    }

    @GetMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId){
        userService.deleteGymUser(userId);
        return "redirect:/Login";
    }
}
