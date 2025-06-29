package ssdd.practicaWeb.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import java.util.List;


@Entity
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "listNutritions")
    private List<Food> listFoods;

    @PreRemove
    private void deleteListFoods(){ //for delete the nutrition's listFoods references before delete the specific nutrition in order to avoid an integrity violation of database
        for(Food food: getListFoods()){
            if(food.getListNutritions() != null){
                food.getListNutritions().remove(this);
            }
        }
    }

    public Nutrition() {
    }

    public Nutrition(String name , String type, List<Food> foods) {
        this.name = name;
        this.type = type;
        this.listFoods = foods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User gymUser) {
        this.user = gymUser;
    }

    public List<Food> getListFoods() {
        return listFoods;
    }

    public void setListFoods(List<Food> listFoods) {
        this.listFoods = listFoods;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
