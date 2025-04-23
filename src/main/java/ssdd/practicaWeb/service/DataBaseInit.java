package ssdd.practicaWeb.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ssdd.practicaWeb.entities.Food;
import ssdd.practicaWeb.entities.GymUser;
import ssdd.practicaWeb.entities.Nutrition;
import ssdd.practicaWeb.entities.Routine;
import ssdd.practicaWeb.repositories.FoodRepository;
import ssdd.practicaWeb.repositories.NutritionRepository;
import ssdd.practicaWeb.repositories.RoutineRepository;
import ssdd.practicaWeb.repositories.UserRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
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

    @Autowired
    NutritionRepository nutritionRepository;

    @Autowired
    FoodRepository foodRepository;

    @PostConstruct
    public void init() throws IOException, SQLException {
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

        ClassPathResource imgFile0 = new ClassPathResource("static/images/emptyUser.png");
        byte[] imageBytes0;
        try (InputStream inputStream = imgFile0.getInputStream()) {
            imageBytes0 = inputStream.readAllBytes();
        }
        Blob imageBlob0 = new SerialBlob(imageBytes0);
        gymUser.setImgUser(imageBlob0);
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


        Nutrition nutrition1 = new Nutrition();
        nutrition1.setName("Deficit");
        nutrition1.setType("Deficit Calorico");
        nutrition1.setGymUser(gymUser);
        nutritionRepository.save(nutrition1);


        Nutrition nutrition2 = new Nutrition();
        nutrition2.setName("Superavit");
        nutrition2.setType("Deficit Calorico");
        nutrition2.setGymUser(gymUser);
        nutritionRepository.save(nutrition2);

        nutrition1.setListFoods(foods1);
        nutrition2.setListFoods(foods2);

        fresa.setListNutritions(List.of(nutrition1));
        foodRepository.save(fresa);
        nueces.setListNutritions(List.of(nutrition1));
        foodRepository.save(nueces);
        huevo.setListNutritions(List.of(nutrition1));
        foodRepository.save(huevo);

        lechuga.setListNutritions(List.of(nutrition2));
        foodRepository.save(lechuga);
        lubina.setListNutritions(List.of(nutrition2));
        foodRepository.save(lubina);
        limon.setListNutritions(List.of(nutrition2));
        foodRepository.save(limon);

        nutritionRepository.save(nutrition1);
        nutritionRepository.save(nutrition2);

        List<Nutrition> listNutrition = new ArrayList<>();
        listNutrition.add(nutrition1);
        listNutrition.add(nutrition2);

        gymUser.setListNutrition(listNutrition);


        userRepository.save(gymUser);
    }
}
