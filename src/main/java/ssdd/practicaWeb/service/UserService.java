package ssdd.practicaWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ssdd.practicaWeb.entities.GymUser;

import ssdd.practicaWeb.repositories.NutritionRepository;
import ssdd.practicaWeb.repositories.RoutineRepository;
import ssdd.practicaWeb.repositories.UserRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
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

    public GymUser createGymUser(GymUser gymUser) throws IOException, SQLException {
        if (gymUser.getImgUser()==null){
            ClassPathResource imgFileDefault = new ClassPathResource("static/images/emptyImage.png");
            byte[] imageBytes;
            try (InputStream inputStream = imgFileDefault.getInputStream()) {
                imageBytes = inputStream.readAllBytes();
            }
            Blob imageBlob = new SerialBlob(imageBytes);
            gymUser.setImgUser(imageBlob);
        }
        userRepository.save(gymUser);
        return gymUser;
    }

    public Optional<GymUser> findById(Long id) {
        return userRepository.findById(id);
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
            GymUser theGymUser1 = theGymUser.get();

            theGymUser1.setUsername(gymUser.getUsername());
            theGymUser1.setAge(gymUser.getAge());
            theGymUser1.setCaloricPhase(gymUser.getCaloricPhase());
            theGymUser1.setGender(gymUser.getGender());
            theGymUser1.setHeight(gymUser.getHeight());
            theGymUser1.setPassword(gymUser.getPassword());
            if (gymUser.getImgUser() != null) {
                theGymUser1.setImgUser(gymUser.getImgUser());
            }
            userRepository.save(theGymUser1);
            return theGymUser1;
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

