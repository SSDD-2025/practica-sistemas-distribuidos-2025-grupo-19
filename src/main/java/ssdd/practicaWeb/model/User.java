package ssdd.practicaWeb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.springframework.web.context.annotation.SessionScope;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


@Entity
@SessionScope
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private Blob imgUser;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;


    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String encodedPassword;

    @NotNull(message = "El peso es obligatorio")
    @Min(value = 1, message = "El peso debe ser mayor que 0 kg")
    private double weight;//Kg

    @NotNull(message = "El peso objetivo es obligatorio")
    @Min(value = 1, message = "El peso objetivo debe ser mayor que 0 kg")
    private double goalWeight;//kg

    @NotNull(message = "La altura es obligatoria")
    @Min(value = 1, message = "La altura debe ser mayor que 0 cm")
    @Max(value = 300, message = "La altura no puede ser mayor de 300 cm")
    private int height;//cm

    @NotBlank(message = "El género es obligatorio")
    private String gender;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 1, message = "La edad debe ser mayor que 0 años")
    @Max(value = 150, message = "La edad no puede ser mayor de 150 años")
    private int age;

    @NotBlank(message = "La morfología es obligatoria")
    private String morphology;

    @NotBlank(message = "La etapa calórica es obligatoria")
    private String caloricPhase;

    //@OneToMany(mappedBy = "gymUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ManyToMany
    private List<Nutrition> nutritionList;// = new ArrayList<>();

    //@OneToMany(mappedBy = "gymUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ManyToMany
    private List<Training> trainingList; //= new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    //Constructor, getters, setters
    public User(String name, String email, String encodedPassword, String... roles) {
        this.name = name;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.roles = List.of(roles);
    }

    public User() {}

    public Boolean isRole(String rol) {
        return this.roles.contains(rol);
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    public List<Nutrition> getNutritionList() {
        return nutritionList;
    }

    public void setNutritionList(List<Nutrition> listNutrition) {
        this.nutritionList = listNutrition;
    }

    public List<Training> getTrainingList() {
        return trainingList;
    }

    public void setTrainingList(List<Training> listRoutine) {
        this.trainingList = listRoutine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getEmail() {return email;}



    public void setEmail(String email) {this.email = email;}

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getGoalWeight() {
        return goalWeight;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setGoalWeight(double goalWeight) {
        this.goalWeight = goalWeight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMorphology() {
        return morphology;
    }

    public void setMorphology(String morphology) {
        this.morphology = morphology;
    }

    public String getCaloricPhase() {
        return caloricPhase;
    }

    public void setCaloricPhase(String caloricPhase) {
        this.caloricPhase = caloricPhase;
    }

    public Blob getImgUser() {return imgUser;}

    public void setImgUser(Blob imgUser) {this.imgUser = imgUser;}

    public void setEncodedPassword(String password) {
        this.encodedPassword = password;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
