package ssdd.practicaWeb.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ssdd.practicaWeb.dtosEdit.CaloricPhase;
import ssdd.practicaWeb.dtosEdit.Gender;
import ssdd.practicaWeb.dtosEdit.Morphology;
import ssdd.practicaWeb.model.User;
import ssdd.practicaWeb.repository.UserRepository;
import ssdd.practicaWeb.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/")
    public String frontPage(Model model, Principal principal) {
        return "index";
    }

    @GetMapping("/index")
    public String index(Model model, Principal principal) {
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return "register";
        } else {
            return "redirect:/index";
        }
    }

    @PostMapping("/register")
    public String createUser(@RequestParam String name,@RequestParam String email,
                             @RequestParam String password, Model model) {

        try {
            // Check if user already exists with the given email
            Optional<User> existingUser = userService.findByEmail(email);

            if (existingUser.isPresent()) {
                // User exists, so we return an error message
                model.addAttribute("error", "El email ya está en uso");
                return "register";
            }

            // Create the user
            User user = userService.createUser(name, email, password, "USER");

            // Save the user in the database
            userService.save(user);

            return "redirect:/login";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ha ocurrido un error.");
            return "login";
        }
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            return "login";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/account")
    public String showProfile(Model model, Principal principal){
        Optional<User> user = userService.findByEmail(principal.getName());
        if(user.isPresent()) {
            model.addAttribute("user",user.get());
            return "account";
        }
        return "redirect:/login";
    }

    @GetMapping("/account/edit")
    public String editeProfileGet(Model model,Principal principal){
        Optional<User> user = userService.findByEmail(principal.getName());
        String originalGender = user.get().getGender();
        String originalMorphology = user.get().getMorphology();
        String originalCaloricPhase = user.get().getCaloricPhase();

        if(user.isPresent()){
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
        return "redirect:/index";
    }

    @PostMapping("/account/edit}")
    public String editProfilePost(@Valid User user, BindingResult result, @RequestParam("imageFile") MultipartFile imageFile, Model model, Principal principal,  HttpServletRequest request) throws IOException {
        Optional<User> existingUser = userService.findByEmail(principal.getName());
        if (result.hasErrors()) {
            model.addAttribute( "mistake", "Por favor, corrige los errores en el formulario.");
            model.addAttribute("user", existingUser.get());


            String originalGender = existingUser.get().getGender();
            String originalMorphology = existingUser.get().getMorphology();
            String originalCaloricPhase = existingUser.get().getCaloricPhase();
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
            model.addAttribute("user",existingUser);
            return "edit";
        }
        if (existingUser.isEmpty()){
            model.addAttribute( "mistake", "El usuario no existe");
            return "index";
        }else{
            User existingUser1 = existingUser.get();

            boolean emailChanged = !existingUser1.getEmail().equals(user.getEmail());
            existingUser1.setEmail(user.getEmail());

            existingUser1.setName(user.getName());
            existingUser1.setEmail(user.getEmail());
            existingUser1.setWeight(user.getWeight());
            existingUser1.setGoalWeight(user.getGoalWeight());
            existingUser1.setHeight(user.getHeight());
            existingUser1.setAge(user.getAge());
            existingUser1.setGender(user.getGender());
            existingUser1.setMorphology(user.getMorphology());
            existingUser1.setCaloricPhase(user.getCaloricPhase());

            if (imageFile != null && !imageFile.isEmpty()) {
                existingUser1.setImgUser(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));

            }

            userService.save(existingUser1);

            if (emailChanged) {
                request.getSession().invalidate();
                SecurityContextHolder.clearContext();
                return "redirect:/login";
            }

            return "redirect:/account";
        }

    }

    @GetMapping("/admin/delete/{userId}")
    public String deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return "redirect:/admin";
    }

    @GetMapping("/account/image")
    public ResponseEntity<byte[]> downloadImage(Principal principal) throws SQLException {

        Optional<User> user = userService.findByEmail(principal.getName());

        if (user.isPresent() && user.get().getImgUser() != null) {
            Blob imageBlob = user.get().getImgUser();
            byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/admin")
    public String admin(Principal principal, Model model) {
        Optional<User> user = userService.findByEmail(principal.getName());

        if (user.isPresent() && user.get().isRole("ADMIN")) {

            List<User> allUsers = userService.findAll();
            List<User> nonAdminUsers = allUsers.stream()
                    .filter(user1 -> !user1.isRole("ADMIN"))
                    .collect(Collectors.toList());

            model.addAttribute("user", user.get());
            model.addAttribute("users", nonAdminUsers);
            return "admin";
        }else {
            return "index";
        }
    }
}
