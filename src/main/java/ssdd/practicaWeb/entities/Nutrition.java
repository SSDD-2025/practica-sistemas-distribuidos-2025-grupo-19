package ssdd.practicaWeb.entities;


import jakarta.persistence.*;


import java.util.List;


@Entity
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private GymUser gymUser;

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

    public GymUser getGymUser() {
        return gymUser;
    }

    public void setGymUser(GymUser gymUser) {
        this.gymUser = gymUser;
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
