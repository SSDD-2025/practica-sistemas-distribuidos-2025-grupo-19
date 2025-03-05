package ssdd.practicaWeb.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssdd.practicaWeb.entities.Food;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.entities.Nutrition;
import ssdd.practicaWeb.entities.Routine;
import ssdd.practicaWeb.repositories.RoutineRepository;
import ssdd.practicaWeb.repositories.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataBaseInit {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    NutritionService nutritionService;
    @Autowired
    FoodService foodService;
    @Autowired
    RoutineRepository routineRepository;
    @PostConstruct
    public void init() throws IOException{
        Food fresa = new Food("fresa","fruta",33);
        Food nueces = new Food("nueces","fruto seco",654);
        Food huevo = new Food("huevo","alimento proteico",155);
        Food lubina = new Food("lubina","pescado",98);
        Food yogurFresa = new Food("yogur fresa","lacteo",59);
        Food leche = new Food("leche","bebida",42);
        Food queso = new Food("queso","lacteo",402);
        Food naranja = new Food("naranja","fruta",47);
        Food pizza = new Food("pizza","pizza",266);
        Food limon = new Food("limon","citrico",29);
        Food  coliflor= new Food("coliflor","verdura",25);
        Food  aquarius= new Food("aquarius","bebida",45);
        Food  lechuga= new Food("lechuga","verdura",15);
        Food  yogurLimon= new Food("yogur limon","lacteo",103);
        Food  costillar= new Food("ribs","carne",395);

        foodService.createFood(fresa);
        foodService.createFood(nueces);
        foodService.createFood(huevo);
        foodService.createFood(lubina);
        foodService.createFood(yogurFresa);
        foodService.createFood(leche);
        foodService.createFood(queso);
        foodService.createFood(naranja);
        foodService.createFood(pizza);
        foodService.createFood(limon);
        foodService.createFood(coliflor);
        foodService.createFood(aquarius);
        foodService.createFood(lechuga);
        foodService.createFood(yogurLimon);
        foodService.createFood(costillar);

        GymUser gymUser = new GymUser("victor","123456");
        gymUser.setAge(22);
        gymUser.setGender("Masculino");
        gymUser.setHeight(180);
        gymUser.setWeight(90);
        gymUser.setGoalWeight(85);
        gymUser.setMorphology("Mesomorfo");
        gymUser.setCaloricPhase("Volumen");
        userRepository.save(gymUser);

        Routine routine1 = new Routine("Pecho","Alta",90,"Press de banca : 4x8-10\n" +
                "                Press inclinado con mancuernas: 4x10\n" +
                "                Fondos en paralelas: 3x10\n" +
                "                Cruces en polea: 4x12" ,"Subir de peso");
        routine1.setGymUser(gymUser);

        Routine routine2 = new Routine("Espalda","Alta",90,"Dominadas: 4x8-10\n" +
                "                Remo con barra: 4x10\n" +
                "                Jalón al pecho en polea: 3x12\n" +
                "                Remo con mancuerna: 3x12" ,"Subir de peso");
        routine2.setGymUser(gymUser);

        Routine routine3 = new Routine("Cardio","Alta",30,"Crunch en máquina: 4x15\n" +
        "                Toques de talón (oblicuos): 3x20\n" +
                "                Rodillo abdominal (ab wheel): 3x10\n" +
                "                Plancha (estática): 3x45s" ,"Bajar de peso");
        routine3.setGymUser(gymUser);

        routineRepository.save(routine1);
        routineRepository.save(routine2);
        routineRepository.save(routine3);

        List<Routine> listRoutine = new ArrayList<>();
        listRoutine.add(routine1);
        listRoutine.add(routine2);
        listRoutine.add(routine3);

        gymUser.setListRoutine(listRoutine);

        List<Food> foods1 = new ArrayList<>();
        foods1.add(fresa);
        foods1.add(nueces);
        foods1.add(huevo);
         List<Nutrition> listNutritionsFood1 = new ArrayList<>();


        List<Food> foods2 = new ArrayList<>();
        foods2.add(lubina);
        foods2.add(leche);
        foods2.add(queso);
        List<Nutrition> listNutritionsFood2 = new ArrayList<>();

        Nutrition nutrition1 = new Nutrition("Deficit","Deficit Calorico",foods1);
        Nutrition nutrition2 = new Nutrition("Superavit","Superavit Calorico",foods2);
        listNutritionsFood1.add(nutrition1);
        listNutritionsFood2.add(nutrition2);

        fresa.setListNutritions( listNutritionsFood1);
        nutrition1.setGymUser(gymUser);
        nutrition2.setGymUser(gymUser);

        List<Nutrition> listNutrition = new ArrayList<>();
        listNutrition.add(nutrition1);
        listNutrition.add(nutrition2);

        gymUser.setListNutrition(listNutrition);


        userRepository.save(gymUser);
    }
}
