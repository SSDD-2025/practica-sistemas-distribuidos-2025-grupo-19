package ssdd.practicaWeb.entities;

public class RoutineDTO {

    private String routineName;
    private String intensity;
    private int duration;
    private String exercises;
    private String goal;
    private Long gymUserId;
    private String gymUserUsername;
    private Long id;

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

    public Long getGymUserId() {
        return gymUserId;
    }

    public void setGymUserId(Long gymUserId) {
        this.gymUserId = gymUserId;
    }

    public String getGymUserUsername() {
        return gymUserUsername;
    }

    public void setGymUserUsername(String gymUserUsername) {
        this.gymUserUsername = gymUserUsername;
    }

    public RoutineDTO(Routine routine){
        this();
        this.setId(routine.getId());
        this.setRoutineName(routine.getRoutineName());
        this.setIntensity(routine.getIntensity());
        this.setDuration(routine.getDuration());
        this.setExercises(routine.getExercises());
        this.setGoal(routine.getGoal());
        this.setGymUserId(routine.getGymUser().getId());
        this.setGymUserUsername(routine.getGymUser().getUsername());
    }

    public RoutineDTO() {
    }
}
