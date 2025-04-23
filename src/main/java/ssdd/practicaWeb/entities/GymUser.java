package ssdd.practicaWeb.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


@Entity

public class GymUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private Blob imgUser;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

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

    @OneToMany(mappedBy = "gymUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Nutrition> listNutrition = new ArrayList<>();

    @OneToMany(mappedBy = "gymUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Routine> listRoutine = new ArrayList<>();

    //Constructor, getters, setters
    //Other details are not compulsory but editable
    public GymUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public GymUser() {}

    public List<Nutrition> getListNutrition() {
        return listNutrition;
    }

    public void setListNutrition(List<Nutrition> listNutrition) {
        this.listNutrition = listNutrition;
    }

    public List<Routine> getListRoutine() {
        return listRoutine;
    }

    public void setListRoutine(List<Routine> listRoutine) {
        this.listRoutine = listRoutine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getGoalWeight() {
        return goalWeight;
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
}
