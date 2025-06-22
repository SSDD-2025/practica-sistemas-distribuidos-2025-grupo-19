package ssdd.practicaWeb.service;

import ssdd.practicaWeb.dto.UserDTO;
import ssdd.practicaWeb.model.Nutrition;
import ssdd.practicaWeb.model.Training;
import  ssdd.practicaWeb.model.User;
import  ssdd.practicaWeb.repository.UserRepository;
import  ssdd.practicaWeb.security.LoginRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import  ssdd.practicaWeb.dto.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserMapper userMapper;

    private UserDTO toUserDTO(User user) {
        return mapper.toUserDTO(user);
    }

    private User toUser(UserDTO userDTO) {
        return mapper.toUser(userDTO);
    }

    public ResponseEntity<Object> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()));

            // Save authentication details in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok("Login successful");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    public User createUser(String name, String email, String pass, String... roles) throws SQLException, IOException {
        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setEncodedPassword(passwordEncoder.encode(pass));
        user.setRoles(List.of(roles));

        ClassPathResource imgFileDefault = new ClassPathResource("static/images/emptyUser.png");
        byte[] imageBytes;
        try (InputStream inputStream = imgFileDefault.getInputStream()) {
            imageBytes = inputStream.readAllBytes();
        }
        Blob imageBlob = new SerialBlob(imageBytes);
        user.setImgUser(imageBlob);

        return user;
    }

    public void updateUserName(Long userId, String newName) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(newName);
        }
    }

    public void updateUserEmail(Long userId, String newEmail) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(newEmail);
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean exist(Long id) {
        return userRepository.existsById(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }


    public boolean existEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    public UserDTO getUser(String name) {
        return mapper.toUserDTO(userRepository.findByName(name).orElseThrow());
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = toUser(userDTO);
        user.setEncodedPassword(passwordEncoder.encode(userDTO.password()));
        user.setRoles(List.of("USER"));

        try {
            ClassPathResource imgFileDefault = new ClassPathResource("static/images/emptyUser.png");
            try (InputStream inputStream = imgFileDefault.getInputStream()) {
                byte[] imageBytes = inputStream.readAllBytes();
                Blob imageBlob = new SerialBlob(imageBytes);
                user.setImgUser(imageBlob);
            }
        } catch (IOException | SQLException e) {

            throw new RuntimeException("Error setting default image", e);
        }

        userRepository.save(user);
        return toUserDTO(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) throws SQLException {

        User user = userRepository.findById(id).orElseThrow();
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (userDTO.email() != null && !userDTO.email().isEmpty()) {
            user.setEmail(userDTO.email());
        } else {
            user.setEmail(user.getEmail());
        }
        if (userDTO.password() != null && !userDTO.password().isEmpty()) {
            user.setEncodedPassword(passwordEncoder.encode(userDTO.password()));
        } else {
            user.setEncodedPassword(user.getEncodedPassword());
        }
        if (userDTO.name() != null && !userDTO.name().isEmpty()) {
            user.setName(userDTO.name());
        } else {
            user.setName(user.getName());
        }
        if (user.getImgUser() != null) {
            user.setImgUser(BlobProxy.generateProxy(user.getImgUser().getBinaryStream(), user.getImgUser().length()));
        } else {
            user.setImgUser(null);
        }

        if (userDTO.age() != 0) {
            user.setAge(userDTO.age());
        } else {
            user.setAge(user.getAge());
        }

        if (userDTO.morphology() != null && !userDTO.morphology().isEmpty()) {
            user.setMorphology(userDTO.name());
        } else {
                user.setMorphology(user.getMorphology());
        }

        if (userDTO.caloricPhase() != null && !userDTO.caloricPhase().isEmpty()) {
            user.setCaloricPhase(userDTO.caloricPhase());
        } else {
            user.setCaloricPhase(user.getCaloricPhase());
        }

        if (userDTO.goalWeight() != 0) {
            user.setName(userDTO.name());
        } else {
            user.setName(user.getName());
        }

        if (userDTO.gender() != null && !userDTO.gender().isEmpty()) {
            user.setGender(userDTO.gender());
        } else {
            user.setGender(user.getGender());
        }

        if (userDTO.height() != 0) {
            user.setHeight(userDTO.height());
        } else {
            user.setHeight(user.getHeight());
        }

        if (userDTO.weight() != 0.0) {
            user.setWeight(userDTO.weight());
        } else {
            user.setWeight(user.getWeight());
        }

        return toUserDTO(userRepository.save(user));
    }

    public InputStreamResource getUserImage(long id) throws SQLException {

        User user = userRepository.findById(id).orElseThrow();

        if (user.getImgUser() != null) {
            return new InputStreamResource(user.getImgUser().getBinaryStream());
        } else {
            throw new NoSuchElementException();
        }
    }

    public void replaceUserImage(long id, InputStream inputStream, long size) {

        User user = userRepository.findById(id).orElseThrow();
        user.setImgUser(BlobProxy.generateProxy(inputStream, size));
        userRepository.save(user);
    }

    public void deleteUserImage(long id) throws IOException, SQLException {
        User user = userRepository.findById(id).orElseThrow();
        if(user.getImgUser() == null){
            throw new NoSuchElementException();
        }
        ClassPathResource imgFileDefault = new ClassPathResource("static/images/emptyUser.png");
        byte[] imageBytesDefault = Files.readAllBytes(imgFileDefault.getFile().toPath());
        Blob imageBlobDefault = new SerialBlob(imageBytesDefault);
        user.setImgUser(imageBlobDefault);
        userRepository.save(user);
    }

    public Collection<UserDTO> getUsers() {
        return mapper.toUserDTOs(userRepository.findAll());
    }

    public Optional<UserDTO> getAuthenticatedUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .map(userMapper::toUserDTO);
        }

        return Optional.empty();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Long getAuthenticatedUserId() {
        return getAuthenticatedUserDto()
                .map(UserDTO::id)
                .orElseThrow(() -> new RuntimeException("User not authenticated"));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

//    public boolean isSubscribedToTraining(String name, Training training) {
//
//        Optional<User> userOpt = userRepository.findByEmail(name);
//        if (userOpt.isPresent()) {
//            return userOpt.get().isSubscribedToTraining(training);
//        } else {
//            return false;
//        }
//    }
//
//    public boolean isSubscribedToNutrition(String name, Nutrition nutrition) {
//        Optional<User> userOpt = userRepository.findByEmail(name);
//        if (userOpt.isPresent()) {
//            return userOpt.get().isSubscribedToNutrition(nutrition);
//        } else {
//            return false;
//        }
//    }


}
