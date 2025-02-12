package ssdd.practicaWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssdd.practicaWeb.entities.GymUser;

import ssdd.practicaWeb.repositories.NutritionRepository;
import ssdd.practicaWeb.repositories.RoutineRepository;
import ssdd.practicaWeb.repositories.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoutineRepository routineRepository;
    @Autowired
    NutritionRepository nutritionRepository;

    public GymUser createGymUser(GymUser gymUser){
        userRepository.save(gymUser);
        return gymUser;
    }

    public GymUser getGymUser(String username){
        Optional<GymUser> gymUser = userRepository.findByUsername(username);
        if (gymUser.isPresent()) {
            return gymUser.get();
        }
        return null;
    }
    public GymUser getGymUser(Long id){
        Optional<GymUser> theUser = userRepository.findById(id);
        if (theUser.isPresent()) {
            return theUser.get();
        }
        return null;
    }
    public Collection <GymUser> getAllGymUser(){
        return userRepository.findAll();
    }
    public GymUser updateGymUser(Long id, GymUser gymUser){
        Optional<GymUser> theGymUser = userRepository.findById(id);
        if(theGymUser.isPresent()) {
            gymUser.setId(id);
            userRepository.save(gymUser);
            return gymUser;
        }
        return null;
    }
    public GymUser deleteGymUser(Long id){
        Optional<GymUser> theGymUser = userRepository.findById(id);
        if (theGymUser.isPresent()) {
            GymUser user = theGymUser.get();
            userRepository.delete(user);
            return user;
        }
        return null;
    }
}

