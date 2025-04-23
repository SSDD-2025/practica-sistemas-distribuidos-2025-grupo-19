package ssdd.practicaWeb.controllers;

import jakarta.validation.Valid;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ssdd.practicaWeb.dtosEdit.CaloricPhase;
import ssdd.practicaWeb.dtosEdit.Gender;
import ssdd.practicaWeb.dtosEdit.Morphology;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.repositories.UserRepository;
import ssdd.practicaWeb.service.UserService;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public String editProfilePost(@Valid GymUser user, BindingResult result, @PathVariable Long userId,  @RequestParam("imageFile") MultipartFile imageFile, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute( "mistake", "Por favor, corrige los errores en el formulario.");
            model.addAttribute("user", user);

            GymUser user1 = userService.getGymUser((userId));
            String originalGender = user1.getGender();
            String originalMorphology = user1.getMorphology();
            String originalCaloricPhase = user1.getCaloricPhase();
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
            model.addAttribute("user",user1);
            return "edit";
        }
        Optional<GymUser> existingUser = userService.findById(userId);
        if (existingUser.isPresent()) {
            GymUser existingUser1 = existingUser.get();

            if (imageFile != null && !imageFile.isEmpty()) {
                user.setImgUser(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            } else {
                user.setImgUser(existingUser1.getImgUser()); // keep previous image
            }

            if (existingUser.isPresent()) {

                userService.updateGymUser(userId, user);
            }
            return "redirect:/users/{userId}";
        }
        return null;
    }

    @GetMapping("/users/delete/{userId}")
    public String deleteUser(@PathVariable Long userId){
        userService.deleteGymUser(userId);
        return "redirect:/Login";
    }

    @GetMapping("/users/image/{userId}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long userId) throws SQLException {

        Optional<GymUser> user = userService.findById(userId);

        if (user.isPresent() && user.get().getImgUser() != null) {
            Blob imageBlob = user.get().getImgUser();
            byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }

        return ResponseEntity.notFound().build();
    }
}
