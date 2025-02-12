package ssdd.practicaWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.entities.Routine;
import ssdd.practicaWeb.repositories.RoutineRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class RoutineService {

    @Autowired
    private RoutineRepository routineRepository;
    @Autowired
    private UserService userService;

    public Routine createRoutine(Routine routine, GymUser user){
        Routine newRoutine = new Routine(routine.getRoutineName(),routine.getIntensity(),routine.getDuration(),routine.getExercises(),routine.getGoal());
        newRoutine.setGymUser(user);
        routineRepository.save(newRoutine);
        return newRoutine;
    }
    public Routine getRoutine(Long id){
        Optional<Routine> theRoutine = routineRepository.findById(id);
        if (theRoutine.isPresent()) {
            Routine routine = theRoutine.get();
            return routine;
        } else {
            return null;
        }
    }
    public Collection <Routine> getAllRoutines(Long id){
        Optional<List<Routine>> listRoutineUser = routineRepository.findByGymUser(userService.getGymUser(id));
        if(listRoutineUser.isPresent()){
            return listRoutineUser.get();
        }
        return null;
    }
    public Routine updateRoutine(Long routineId, Routine routine, GymUser user){
        Optional<Routine> theRoutine = routineRepository.findById(routineId);
        if(theRoutine.isPresent()) {
            routine.setId(routineId);
            routine.setGymUser(user);
            routineRepository.save(routine);
            return routine;
        }

        return null;
    }

    public Routine deleteRoutine(Long id){
        Optional<Routine> theRoutine = routineRepository.findById(id);
        if (theRoutine.isPresent()) {
            Routine routine = theRoutine.get();
            GymUser user = routine.getGymUser();
            user.getListRoutine().remove(routine);
            routineRepository.delete(routine);
            return routine;
        }
        return null;
    }
    public List<Routine> getRoutinesUser(GymUser user){
        Optional<List<Routine>> routines = routineRepository.findByGymUser(user);
        if(routines.isPresent()){
            return routines.get();
        }
        return null;
    }
}
