package ssdd.practicaWeb.dto;

import java.util.List;


public record FoodDTO(
    Long id,
    String name,
    int calories,
    String type,
    List<Long> listIdsNutrition// List of nutrition's ids
    ) {
}

