package ssdd.practicaWeb.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ssdd.practicaWeb.model.Food;
import ssdd.practicaWeb.model.User;
import ssdd.practicaWeb.model.Nutrition;
import ssdd.practicaWeb.model.Training;
import ssdd.practicaWeb.repository.FoodRepository;
import ssdd.practicaWeb.repository.NutritionRepository;
import ssdd.practicaWeb.repository.TrainingRepository;
import ssdd.practicaWeb.repository.UserRepository;

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
    private PasswordEncoder passwordEncoder;

    @Autowired
    FoodService foodService;

    @Autowired
    TrainingRepository routineRepository;

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

        User admin = new User("admin", "admin@admin.com", passwordEncoder.encode("adminpass"), "ADMIN", "USER");
        userRepository.save(admin);


        User gymUser = new User("victor","user@user.com", passwordEncoder.encode("pass"),"USER");
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

        User gymUser2 = new User("david","david@david.com", passwordEncoder.encode("pass2"),"USER");
        gymUser2.setAge(26);
        gymUser2.setGender("Masculino");
        gymUser2.setHeight(182);
        gymUser2.setWeight(77);
        gymUser2.setGoalWeight(82);
        gymUser2.setMorphology("Ectomorfo");
        gymUser2.setCaloricPhase("Volumen");

        ClassPathResource imgFile2 = new ClassPathResource("static/images/images.jpeg");
        byte[] imageBytes2;
        try (InputStream inputStream = imgFile2.getInputStream()) {
            imageBytes2 = inputStream.readAllBytes();
        }
        Blob imageBlob2 = new SerialBlob(imageBytes2);
        gymUser2.setImgUser(imageBlob2);
        userRepository.save(gymUser2);

        Training routine1 = new Training("Pecho","Alta",90,"Press de banca : 4x8-10\n" +
                "                Press inclinado con mancuernas: 4x10\n" +
                "                Fondos en paralelas: 3x10\n" +
                "                Cruces en polea: 4x12" ,"Subir de peso");
        routine1.setUser(gymUser);

        Training routine2 = new Training("Espalda","Alta",90,"Dominadas: 4x8-10\n" +
                "                Remo con barra: 4x10\n" +
                "                Jalón al pecho en polea: 3x12\n" +
                "                Remo con mancuerna: 3x12" ,"Subir de peso");
        routine2.setUser(gymUser);

        Training routine3 = new Training("Cardio","Alta",30,"Crunch en máquina: 4x15\n" +
        "                Toques de talón (oblicuos): 3x20\n" +
                "                Rodillo abdominal (ab wheel): 3x10\n" +
                "                Plancha (estática): 3x45s" ,"Bajar de peso");
        routine3.setUser(gymUser);

        routineRepository.save(routine1);
        routineRepository.save(routine2);
        routineRepository.save(routine3);

        List<Training> listRoutine = new ArrayList<>();
        listRoutine.add(routine1);
        listRoutine.add(routine2);
        listRoutine.add(routine3);

        gymUser.setTrainingList(listRoutine);

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
        nutrition1.setUser(gymUser);
        nutrition1.setListFoods(foods1);
        nutritionRepository.save(nutrition1);


        Nutrition nutrition2 = new Nutrition();
        nutrition2.setName("Superavit");
        nutrition2.setType("Deficit Calorico");
        nutrition2.setUser(gymUser);
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

        gymUser.setNutritionList(listNutrition);


        userRepository.save(gymUser);
    }
}
