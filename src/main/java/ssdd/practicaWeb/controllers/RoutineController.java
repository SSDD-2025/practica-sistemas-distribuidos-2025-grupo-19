package ssdd.practicaWeb.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ssdd.practicaWeb.dtosEdit.Goal;
import ssdd.practicaWeb.dtosEdit.Intensity;
import ssdd.practicaWeb.entities.Routine;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.repositories.RoutineRepository;
import ssdd.practicaWeb.service.RoutineService;
import ssdd.practicaWeb.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RoutineController {
    @Autowired
    private RoutineService routineService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoutineRepository routineRepository;


    @GetMapping("/routines")
    public String showAllRoutines(Model model, @RequestParam("userId") Long userId){
        model.addAttribute("routines",routineService.getAllRoutines(userId));
        GymUser user = userService.getGymUser(userId);
        if(user != null){
            model.addAttribute("userId",user.getId());
            return "routines";
        }
        return "redirect:/Portada";
    }
    @GetMapping("/routines/{routineId}")
    public String showRoutine(Model model, @PathVariable Long routineId, @RequestParam("userId") Long userId){
        Routine routine = routineService.getRoutine(routineId);
        GymUser user = userService.getGymUser(userId);
        if(routine == null){
            return "redirect:/frontPage?userId=" + user.getId();
        }
        if(user == null){
            return "redirect:/Login";
        }
        model.addAttribute("routine",routine);
        model.addAttribute("userId",user.getId());
        return "showRoutine";
    }
    @GetMapping("/routines/createRoutine")
    public String createRoutine(Model model, @RequestParam("userId") Long userId){
        model.addAttribute("routine",new Routine());
        GymUser user = userService.getGymUser(userId);
        if(user != null){
            model.addAttribute("userId",user.getId());
            return "createRoutine";
        }
        return "redirect:/Login";
    }
    @PostMapping("/routines/createRoutine")
    public String createRoutinePost(@Valid Routine routine, BindingResult result, @RequestParam("userId") Long userId, Model model){
        if (result.hasErrors()) {
            model.addAttribute( "mistake", "Por favor, corrige los errores en el formulario.");
            model.addAttribute("userId",userId);
            return "createRoutine";
        }

        GymUser user = userService.getGymUser(userId);
        if (user != null){
            routine.setGymUser(user);
            routineService.createRoutine(routine,user);
            return "redirect:/routines?userId=" + userId;
        }
        return "redirect:/Login";
    }

    @GetMapping("/routines/editRoutine/{routineId}")
    public String editRoutine(Model model, @PathVariable Long routineId, @RequestParam("userId") Long userId){
        Routine routine = routineService.getRoutine(routineId);
        GymUser user = userService.getGymUser(userId);
        if(user == null){
            return "redirect:/Login";
        }
        if(routine == null){
            return "redirect:/frontPage?userId=" + user.getId();
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

        model.addAttribute("routine",routine);
        model.addAttribute("userId",userId);
        return "editRoutine";
    }
    @PostMapping("/routines/editRoutine/{routineId}")
    public String editRoutinePost(@Valid Routine routine, BindingResult result, @PathVariable Long routineId, @RequestParam("userId") Long userId, Model model){

        if (result.hasErrors()) {
            Routine routineOriginal = routineService.getRoutine(routineId);
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

            model.addAttribute("routine",routineOriginal);
            model.addAttribute("userId",userId);

            model.addAttribute( "mistake", "Por favor, corrige los errores en el formulario.");
            model.addAttribute("userId", userId);
            return "editRoutine";
    }
        GymUser user = userService.getGymUser(userId);
        routineService.updateRoutine(routineId,routine, user);
        return "redirect:/routines?userId=" + userId;
    }
    @GetMapping("/routines/delete/{routineId}")
    public  String deleteRoutinePost(@PathVariable Long routineId, @RequestParam("userId") Long userId){
        routineService.deleteRoutine(routineId);
        GymUser user = userService.getGymUser(userId);
        if(user == null){
            return "redirect:/Login";
        }
        return "redirect:/routines?userId=" + user.getId();
    }
}

