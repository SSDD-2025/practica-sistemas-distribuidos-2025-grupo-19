package ssdd.practicaWeb.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ssdd.practicaWeb.entities.*;
import ssdd.practicaWeb.service.RoutineService;
import ssdd.practicaWeb.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/routines")
public class RoutineRESTController {
    @Autowired
    private RoutineService routineService;
    @Autowired
    private UserService userService;
    @GetMapping("/all/{userId}")
    public ResponseEntity<Collection<RoutineDTO>> getAllRoutines(@PathVariable Long userId){
        List<RoutineDTO> list = new ArrayList<>();
        List<Routine> routines = (List<Routine>) routineService.getAllRoutines(userId);
        for(Routine routine: routines){
            list.add(new RoutineDTO(routine));
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<RoutineDTO> createRoutine(@RequestBody Routine routine , @PathVariable Long userId){
        GymUser user = userService.getGymUser(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        Routine routineObt = routineService.createRoutine(routine,user);
        return ResponseEntity.status(201).body(new RoutineDTO(routineObt));
    }
    @GetMapping("/{id}")
    public ResponseEntity<RoutineDTO> getRoutine(@PathVariable Long id){
        Routine routine = routineService.getRoutine(id);
        if(routine == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new RoutineDTO(routine));
    }
    @PutMapping("/{userId}/{id}")
    public ResponseEntity<RoutineDTO> updateRoutine(@PathVariable Long id, @PathVariable Long userId, @RequestBody Routine routine){
        GymUser user = userService.getGymUser(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        Routine updated = routineService.updateRoutine(id,routine,user);
        if(updated == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new RoutineDTO(updated));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id){
        routineService.deleteRoutine(id);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{userId}/{id}")
    public ResponseEntity<RoutineDTO> patchRoutine(@PathVariable Long id, @PathVariable Long userId, @RequestBody Routine parcialRoutine){
        GymUser user = userService.getGymUser(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        Routine routine = routineService.getRoutine(id);
        if (routine == null){
            return ResponseEntity.notFound().build();
        }
        if (parcialRoutine.getRoutineName() != null) {
            routine.setRoutineName(parcialRoutine.getRoutineName());
        }
        if (parcialRoutine.getIntensity() != null) {
            routine.setIntensity(parcialRoutine.getIntensity());
        }
        if (parcialRoutine.getDuration() != 0) {
            routine.setDuration(parcialRoutine.getDuration());
        }
        if(parcialRoutine.getExercises() != null) {
            routine.setExercises(parcialRoutine.getExercises());
        }
        if(parcialRoutine.getGoal() != null) {
            routine.setGoal(parcialRoutine.getGoal());
        }
        Routine updated = routineService.updateRoutine(id,routine, user);
        return ResponseEntity.ok(new RoutineDTO(updated));
    }
}
