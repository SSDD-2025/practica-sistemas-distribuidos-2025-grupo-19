package ssdd.practicaWeb.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.entities.GymUserDTO;
import ssdd.practicaWeb.entities.Nutrition;
import ssdd.practicaWeb.entities.Routine;
import ssdd.practicaWeb.service.NutritionService;
import ssdd.practicaWeb.service.RoutineService;
import ssdd.practicaWeb.service.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRESTController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoutineService routineService;
    @Autowired
    private NutritionService nutritionService;
    @GetMapping
    public ResponseEntity<Collection<GymUserDTO>> getAllUsers(){
        List<GymUserDTO> list = new ArrayList<>();
        List<GymUser> gymUsers = (List<GymUser>) userService.getAllGymUser();
        for(GymUser user: gymUsers){
            List<Routine> routines = routineService.getRoutinesUser(user);
            List<Nutrition> nutritions = nutritionService.getNutritionsUser(user);
            list.add(new GymUserDTO(user,nutritions,routines));
        }
        return ResponseEntity.ok(list);
    }
    @PostMapping
    public ResponseEntity<GymUserDTO> createUser(@RequestBody GymUser user) throws SQLException, IOException {
        GymUser userObt = userService.createGymUser(user);
        return ResponseEntity.status(201).body(new GymUserDTO(userObt));
    }
    @GetMapping("/{id}")
    public ResponseEntity<GymUserDTO> getUser(@PathVariable Long id){
        GymUser user = userService.getGymUser(id);
        List<Routine> routines = routineService.getRoutinesUser(user);
        List<Nutrition> nutritions = nutritionService.getNutritionsUser(user);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new GymUserDTO(user,nutritions,routines));
    }
    @PutMapping("/{id}")
    public ResponseEntity<GymUserDTO> updateUser(@PathVariable Long id, @RequestBody GymUser user){
        GymUser updated = userService.updateGymUser(id,user);
        List<Routine> routines = routineService.getRoutinesUser(user);
        List<Nutrition> nutritions = nutritionService.getNutritionsUser(user);
        if(updated == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new GymUserDTO(updated,nutritions,routines));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteGymUser(id);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<GymUserDTO> patchUser(@PathVariable Long id, @RequestBody GymUser parcialUser){
        GymUser user = userService.getGymUser(id);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        if(parcialUser.getUsername() != null){
            user.setUsername(parcialUser.getUsername());
        }
        if(parcialUser.getPassword() != null){
            user.setPassword(parcialUser.getPassword());
        }
        if(parcialUser.getWeight() != 0){
            user.setWeight(parcialUser.getWeight());
        }
        if(parcialUser.getGoalWeight() != 0){
            user.setGoalWeight(parcialUser.getGoalWeight());
        }
        if(parcialUser.getHeight() != 0){
            user.setHeight(parcialUser.getHeight());
        }
        if(parcialUser.getGender() != null){
            user.setGender(parcialUser.getGender());
        }
        if(parcialUser.getAge() != 0){
            user.setAge(parcialUser.getAge());
        }
        if(parcialUser.getMorphology() != null){
            user.setMorphology(parcialUser.getMorphology());
        }
        if(parcialUser.getCaloricPhase() != null){
            user.setCaloricPhase(parcialUser.getCaloricPhase());
        }
        userService.updateGymUser(id,user);
        return ResponseEntity.ok(new GymUserDTO(user));
    }
}