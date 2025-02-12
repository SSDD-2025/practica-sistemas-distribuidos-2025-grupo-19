package ssdd.practicaWeb.entities;

import java.util.ArrayList;
import java.util.List;

public class NutritionDTO {

    private Long id;
    private String name;
    private String type;
    private Long gymUserId;
    private String gymUserUsername;
    private List<String> listNameFood;//List of food's names

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<String> getListNameFood() {
        return listNameFood;
    }

    public void setListNameFood(List<String> listNameFood) {
        this.listNameFood = listNameFood;
    }

    public NutritionDTO() {
    }

    public NutritionDTO(Nutrition nutrition){
        this();
        this.setId(nutrition.getId());
        this.setName(nutrition.getName());
        this.setType(nutrition.getType());
        List<String> list = new ArrayList<>();
        if(!nutrition.getListFoods().isEmpty()){
            for(Food food: nutrition.getListFoods()){
                list.add(food.getName());
            }
        }
        this.setListNameFood(list);
        this.setGymUserId(nutrition.getGymUser().getId());
        this.setGymUserUsername(nutrition.getGymUser().getUsername());
    }
}
