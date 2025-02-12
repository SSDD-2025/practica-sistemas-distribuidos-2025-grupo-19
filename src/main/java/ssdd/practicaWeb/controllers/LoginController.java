package ssdd.practicaWeb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.repositories.UserRepository;
import ssdd.practicaWeb.service.UserService;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/Login")
    public String InterfaceLogin(Model model){
        model.addAttribute("user",new GymUser());
        model.addAttribute("mistake", "");
        return "login";
    }
    @PostMapping("/Login")
    public String goFrontPage(GymUser user, Model model) {
        GymUser gymUser = userService.getGymUser(user.getUsername());
        if(gymUser == null){
            userService.createGymUser(user);
            return "redirect:/FrontPage?userId=" + user.getId();
        }else if (!(gymUser.getUsername().equals(user.getUsername())&&(gymUser.getPassword().equals(user.getPassword())))){
            model.addAttribute("mistake", "El usuario o la contraseña son incorrectos");
            return "login";
        }
        return "redirect:/FrontPage?userId=" + gymUser.getId();

    }
  
    @GetMapping("/FrontPage")
    public String InterfaceFrontPage(Model model, @RequestParam("userId") Long userId) {
        model.addAttribute("user",userService.getGymUser(userId));
        return "frontPage";
    }

}
