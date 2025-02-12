package ssdd.practicaWeb.entities;

import java.util.ArrayList;
import java.util.List;


public class FoodDTO {
    private Long id;
    private String name;
    private int calories;
    private String type;
    private List<Long> listIdsNutrition;// List of nutrition's ids
    public List<Long> getListIdsNutrition() {
        return listIdsNutrition;
    }

    public void setListIdsNutrition(List<Long> listIdsNutrition) {
        this.listIdsNutrition = listIdsNutrition;
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

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FoodDTO() {
    }

    public FoodDTO(Food food) {
        this();
        this.setId(food.getId());
        this.setName(food.getName());
        this.setCalories(food.getCalories());
        this.setType(food.getType());
        List<Long> list = new ArrayList<>();
        if (food.getListNutritions()!=null) {
            for (Nutrition nutrition : food.getListNutritions()) {
                list.add(nutrition.getId());
            }
        }
        this.setListIdsNutrition(list);
    }
}

