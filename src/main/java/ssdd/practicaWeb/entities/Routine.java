package ssdd.practicaWeb.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre de la rutina no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre de la rutina debe tener entre 3 y 50 caracteres")
    private String routineName;

    @NotBlank(message = "La intensidad no puede estar vacía")
    @Pattern(regexp = "^(Baja|Moderada|Alta)$", message = "La intensidad debe ser Baja, Media o Alta")
    private String intensity;

    @Min(value = 1, message = "La duración debe ser al menos de 1 minuto")
    @Max(value = 300, message = "La duración no puede superar los 300 minutos")
    private int duration;

    @NotBlank(message = "Los ejercicios no pueden estar vacíos")
    @Size(max = 500, message = "Los ejercicios no deben superar los 500 caracteres")
    private String exercises;

    @NotBlank(message = "El objetivo no puede estar vacío")
    @Pattern(regexp = "^(Bajar de peso|Mantenerse|Subir de peso)$", message = "El objetivo debe ser Bajar de peso, Mantenerse o Subir de peso")
    private String goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private GymUser gymUser;

    public Routine(String routineName, String intensity, int duration, String exercises, String goal) {
        this.routineName = routineName;
        this.intensity = intensity;
        this.duration = duration;
        this.exercises = exercises;
        this.goal = goal;
    }

    public Routine() {
    }

    public GymUser getGymUser() {
        return gymUser;
    }

    public void setGymUser(GymUser gymUser) {
        this.gymUser = gymUser;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }
}
