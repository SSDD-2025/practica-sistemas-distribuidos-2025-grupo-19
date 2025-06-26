package ssdd.practicaWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssdd.practicaWeb.dto.TrainingDTO;
import ssdd.practicaWeb.dto.TrainingMapper;
import ssdd.practicaWeb.model.User;
import ssdd.practicaWeb.model.Training;
import ssdd.practicaWeb.repository.TrainingRepository;
import ssdd.practicaWeb.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingMapper trainingMapper;

    public Training createTraining(Training training, User user) {
        Training newTraining = new Training(training.getName(),training.getIntensity(),training.getDuration(),training.getExercises(),training.getGoal());
        newTraining.setUser(user);
        trainingRepository.save(newTraining);
        return newTraining;
    }
    public Training getTraining(Long id){
        Optional<Training> theRoutine = trainingRepository.findById(id);
        if (theRoutine.isPresent()) {
            Training routine = theRoutine.get();
            return routine;
        } else {
            return null;
        }
    }
    public Collection <Training> getAllRoutines(){
        Optional<List<Training>> listRoutineUser = Optional.of(trainingRepository.findAll());
        if(listRoutineUser.isPresent()){
            return listRoutineUser.get();
        }
        return null;
    }

    public Collection<TrainingDTO> getAllDtoTrainings(){
        return trainingMapper.toDTOs(trainingRepository.findAll());
    }

    public Training updateRoutine(Long trainingId, Training training){
        Optional<Training> theRoutine = trainingRepository.findById(trainingId);
        if(theRoutine.isPresent()) {
            training.setId(trainingId);
            training.setUser(theRoutine.get().getUser());
            trainingRepository.save(training);
            return training;
        }

        return null;
    }

    public TrainingDTO replaceTraining(Long trainingId, TrainingDTO trainingDTO){
        Training oldTraining = trainingRepository.findById(trainingId).orElseThrow();
        if (trainingRepository.findById(trainingId).isPresent()) {
            Training training = toDomain(trainingDTO);
            training.setId(trainingId);
            training.setUser(oldTraining.getUser());
            trainingRepository.save(training);
            return toDTO(training);
        } else {
            throw new NoSuchElementException();
        }

    }

    public TrainingDTO getDtoTraining(long trainingId){
        return trainingMapper.toDTO(trainingRepository.findById(trainingId).get());
    }

    public Training deleteTraining(Long id){
        Optional<Training> theRoutine = trainingRepository.findById(id);
        if (theRoutine.isPresent()) {
            Training training = theRoutine.get();

            List<User> usersWithTraining = userRepository.findByTrainingListContaining(training);
            for (User user : usersWithTraining) {
                user.getTrainingList().remove(training);
                userRepository.save(user);
            }

            trainingRepository.delete(training);
            return training;
        }
        return null;
    }

    public void subscribeTraining(Long trainingId , User user) {
        Optional<Training> training = trainingRepository.findById(trainingId);
        if (training.isPresent()) {
            user.getTrainingList().add(training.get());
            userRepository.save(user);
        }
    }

    public void unsubscribeTraining(Long trainingId, User user) {
        Optional<Training> training = trainingRepository.findById(trainingId);
        if (training.isPresent()) {
            user.getTrainingList().remove(training.get());
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long trainingId, Authentication authentication) {
        return trainingRepository.findWithUserById(trainingId)
                .map(routine -> {
                    User user = routine.getUser();
                    return user != null && authentication.getName().equals(user.getEmail());
                })
                .orElse(false);
    }

    public  User getAuthenticationUser (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null ) {
            Optional<User> user = userRepository.findByEmail(authentication.getName());
            if (user.isPresent()){
                return user.get();
            }
        }
        return null;
    }

    public Collection<TrainingDTO> getAllDtoUserTrainings() {
        User currentUser = getAuthenticationUser();
        if (currentUser != null) {
            List<Training> trainings = currentUser.getTrainingList();

            return trainings.stream()
                    .map(trainingMapper::toDTO)
                    .toList();
        }

        return null;
    }

    public TrainingDTO toDTO(Training training) {
        return trainingMapper.toDTO(training);
    }

    public Training toDomain(TrainingDTO trainingDTO) {
        return trainingMapper.toDomain(trainingDTO);
    }

    public boolean subscribeTrainingDTO(Long trainingId, String name) {
        User user = userRepository.findByEmail(name)
                .orElseThrow(() -> new IllegalArgumentException("USer not found"));
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found"));

        if (user.getTrainingList().contains(training)) {
            return true;
        }

        user.getTrainingList().add(training);
        userRepository.save(user);
        return false;
    }

    public boolean unsubscribeTrainingDTO(Long trainingId, String name) {
        User user = userRepository.findByEmail(name)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found"));

        if (!user.getTrainingList().contains(training)) {
            return false;
        }

        user.getTrainingList().remove(training);
        userRepository.save(user);
        return true;
    }

    public List<TrainingDTO> getPaginatedTrainingsDTO(int page, int limit) {
        return trainingRepository
                .findAll(PageRequest.of(page, limit))
                .map(trainingMapper::toDTO)
                .toList();
    }
}
