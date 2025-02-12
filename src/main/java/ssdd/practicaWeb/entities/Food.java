package ssdd.practicaWeb.entities;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private int calories;
    private String type;
    @ManyToMany
    @JoinTable(
            name = "food_nutrition",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "nutrition_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"food_id", "nutrition_id"})
    )
    private List<Nutrition> listNutritions;

    public Food(String name, String type, int calories) {
        this.name = name;
        if(type == null){
            this.type = "comida";
        }else{
            this.type = type;
        }
        this.calories = calories;
    }

    public Food() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Nutrition> getListNutritions() {
        return listNutritions;
    }

    public void setListNutritions(List<Nutrition> listNutritions) {
        this.listNutritions = listNutritions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
