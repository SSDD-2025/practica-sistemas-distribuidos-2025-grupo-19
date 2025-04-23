package ssdd.practicaWeb.entities;

import java.util.ArrayList;
import java.util.List;


public class GymUserDTO {
    private Long id;
    private String userImage;
    private String username;
    private String password;
    private double weight;
    private double goalWeight;
    private int height;
    private String gender;
    private int age;
    private String morphology;
    private String caloricPhase;



    private List<Long> listIdsNutrition;

    private List<Long> listIdsRoutine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
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

    public List<Long> getListIdsNutrition() {
        return listIdsNutrition;
    }

    public void setListIdsNutrition(List<Long> listIdsNutrition) {
        this.listIdsNutrition = listIdsNutrition;
    }

    public List<Long> getListIdsRoutine() {
        return listIdsRoutine;
    }

    public void setListIdsRoutine(List<Long> listIdsRoutine) {
        this.listIdsRoutine = listIdsRoutine;
    }

    public GymUserDTO() {
    }
    public GymUserDTO(GymUser user) {
        this();
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setWeight(user.getWeight());
        this.setGoalWeight(user.getGoalWeight());
        this.setHeight(user.getHeight());
        this.setGender(user.getGender());
        this.setAge(user.getAge());
        this.setMorphology(user.getMorphology());
        this.setCaloricPhase(user.getCaloricPhase());
        List<Long> listRoutine = new ArrayList<>();
        if(!user.getListRoutine().isEmpty()){
            for(Routine routine: user.getListRoutine()){
                listRoutine.add(routine.getId());
            }
        }
        this.setListIdsRoutine(listRoutine);
        List<Long> listNutrition = new ArrayList<>();
        if(!user.getListNutrition().isEmpty()){
            for(Nutrition nutrition: user.getListNutrition()){
                listNutrition.add(nutrition.getId());
            }
        }
        this.setListIdsNutrition(listNutrition);
    }

    public GymUserDTO(GymUser user, List<Nutrition> nutritions, List<Routine> routines) {
        this();
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setWeight(user.getWeight());
        this.setGoalWeight(user.getGoalWeight());
        this.setHeight(user.getHeight());
        this.setGender(user.getGender());
        this.setAge(user.getAge());
        this.setMorphology(user.getMorphology());
        this.setCaloricPhase(user.getCaloricPhase());
        List<Long> listRoutine = new ArrayList<>();
        if(!routines.isEmpty()){
            for(Routine routine: routines){
                listRoutine.add(routine.getId());
    }
}
        this.setListIdsRoutine(listRoutine);
        List<Long> listNutrition = new ArrayList<>();
        if(!nutritions.isEmpty()){
            for(Nutrition nutrition: nutritions){
                listNutrition.add(nutrition.getId());
            }
        }
        this.setListIdsNutrition(listNutrition);
    }
}
