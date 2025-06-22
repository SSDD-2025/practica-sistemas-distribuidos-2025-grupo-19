package ssdd.practicaWeb.dto;

import java.util.List;

public record NutritionDTO (
     Long id,
     String name,
     String type,
     Long userId,
     List<Long> listIdsFood){

}
