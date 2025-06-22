package ssdd.practicaWeb.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ssdd.practicaWeb.dtosEdit.Goal;
import ssdd.practicaWeb.dtosEdit.Intensity;
import ssdd.practicaWeb.model.Training;
import ssdd.practicaWeb.model.User;
import ssdd.practicaWeb.service.TrainingService;
import ssdd.practicaWeb.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TrainingController {
    @Autowired
    private TrainingService trainingService;
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

    @GetMapping("/trainings")
    public String showAllRoutines(Model model) {
        model.addAttribute("trainings", trainingService.getAllRoutines());
        return "trainings";
    }

    @GetMapping("/myTrainings")
    public String showMyRoutines(Model model, Principal principal) {
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent() && !user.get().isRole("ADMIN")) {
                model.addAttribute("trainings", user.get().getTrainingList());
                return "myTrainings";
            } else{
                return "redirect:/trainings";
            }
        }else{
            return "redirect:/login";
        }
    }

    @GetMapping("/trainings/{trainingId}")
    public String showRoutine(Model model, @PathVariable Long trainingId, Principal principal) {
        Training training = trainingService.getTraining(trainingId);

        if (training == null) {
            return "redirect:/trainings";
        }

        model.addAttribute("training", training);

        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                Boolean isAdmin = user.get().isRole("ADMIN");

                // Avoid NullPointerException if it hasn`t author
                Boolean canEdit = isAdmin || (training.getUser() != null && training.getUser().getId().equals(user.get().getId()));
                boolean isSubscribed = user.get().getTrainingList().contains(training);


                model.addAttribute("subscribed", isSubscribed);
                model.addAttribute("logged", true);
                model.addAttribute("canEdit", canEdit);
            }
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("admin", false); // if it`s not logged he cant be "admin"
        }

        return "showRoutine";
    }
    @GetMapping("/trainings/createTraining")
    public String createRoutine(Model model, Principal principal) {
        if(principal != null){
            model.addAttribute("training",new Training());
            return "createRoutine";
        }
        return "redirect:/login";
    }
    @PostMapping("/trainings/createTraining")
    public String createRoutinePost(@Valid Training training, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute( "mistake", "Por favor, corrige los errores en el formulario.");
            model.addAttribute("training",new Training());
            return "createRoutine";
        }

        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent()) {
                if (user.get().isRole("ADMIN")) {
                    trainingService.createTraining(training, null);
                } else {
                    trainingService.createTraining(training, user.get());
                }
                return "redirect:/trainings";
            }
        }
        return "redirect:/trainings";
    }

    @GetMapping("/trainings/editTraining/{trainingId}")
    public String editRoutine(Model model, @PathVariable Long trainingId) {
        Training routine = trainingService.getTraining(trainingId);

        if(routine == null){
            return "redirect:/trainings";
        }

        String originalIntensity = routine.getIntensity();
        String originalGoal = routine.getGoal();

        List<Intensity> intensities = new ArrayList<>();
        intensities.add(new Intensity("Baja", "Baja".equals(originalIntensity)));
        intensities.add(new Intensity("Moderada", "Moderada".equals(originalIntensity)));
        intensities.add(new Intensity("Alta", "Alta".equals(originalIntensity)));


        List <Goal> goals = new ArrayList<>();
        goals.add(new Goal("Bajar de peso", "Bajar de peso".equals(originalGoal)));
        goals.add(new Goal("Mantenerse", "Mantenerse".equals(originalGoal)));
        goals.add(new Goal("Subir de peso", "Subir de peso".equals(originalGoal)));

        model.addAttribute("intensities", intensities);
        model.addAttribute("goals", goals);

        model.addAttribute("training",routine);
        return "editRoutine";
    }
    @PostMapping("/trainings/editTraining/{trainingId}")
    public String editRoutinePost(@Valid Training training, BindingResult result, @PathVariable Long trainingId, Model model, Principal principal) {

        if (result.hasErrors()) {
            Training routineOriginal = trainingService.getTraining(trainingId);
            String originalIntensity = routineOriginal.getIntensity();
            String originalGoal = routineOriginal.getGoal();

            List<Intensity> intensities = new ArrayList<>();
            intensities.add(new Intensity("Baja", "Baja".equals(originalIntensity)));
            intensities.add(new Intensity("Moderada", "Moderada".equals(originalIntensity)));
            intensities.add(new Intensity("Alta", "Alta".equals(originalIntensity)));


            List <Goal> goals = new ArrayList<>();
            goals.add(new Goal("Bajar de peso", "Bajar de peso".equals(originalGoal)));
            goals.add(new Goal("Mantenerse", "Mantenerse".equals(originalGoal)));
            goals.add(new Goal("Subir de peso", "Subir de peso".equals(originalGoal)));

            model.addAttribute("intensities", intensities);
            model.addAttribute("goals", goals);

            model.addAttribute("training",routineOriginal);

            model.addAttribute( "mistake", "Por favor, corrige los errores en el formulario.");
            return "editRoutine";
        }

        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            Training optionalTraining = trainingService.getTraining(trainingId);
            if (optionalTraining!=null) {
                if (user.isPresent()) {
                    trainingService.updateRoutine(trainingId, training);
                    return "redirect:/trainings/" + trainingId;
                }else{
                    return "redirect:/login";
                }
            }
        }

        return "redirect:/login";
    }

    @GetMapping("/trainings/delete/{trainingId}")
    public  String deleteRoutinePost(@PathVariable Long trainingId) {
        trainingService.deleteTraining(trainingId);
        return "redirect:/trainings";
    }


    @GetMapping("/trainings/subscribe/{trainingId}")
    public String subscribeToRoutine(@PathVariable Long trainingId, Principal principal) {
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent() && user.get().getTrainingList().contains(trainingService.getTraining(trainingId))) {
                trainingService.subscribeTraining(trainingId, user.get());
            }
        }
        return "redirect:/trainings/" + trainingId;
    }

    @GetMapping("/trainings/unsubscribe/{trainingId}")
    public String unsubscribeFromRoutine(@PathVariable Long trainingId, Principal principal) {
        if (principal != null) {
            Optional<User> user = userService.findByEmail(principal.getName());
            if (user.isPresent() &&  !user.get().getTrainingList().contains(trainingService.getTraining(trainingId))) {
                trainingService.unsubscribeTraining(trainingId, user.get());
            }
        }
        return "redirect:/trainings/" + trainingId;
    }
}

