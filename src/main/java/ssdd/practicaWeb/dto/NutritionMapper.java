package ssdd.practicaWeb.dto;

import ssdd.practicaWeb.model.Food;
import ssdd.practicaWeb.model.Nutrition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface NutritionMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "listIdsFood", source = "listFoods")
    NutritionDTO toDTO(Nutrition nutrition);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "listIdsFood", source = "listFoods")
    List<NutritionDTO> toDTOs(Collection<Nutrition> nutritions);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "listFoods", ignore = true)
    Nutrition toDomain(NutritionDTO nutritionDTO);

    //method to change List<Food> to List<Long>
    default List<Long> map(List<Food> foods) {
        if (foods == null) return Collections.emptyList();
        return foods.stream().map(Food::getId).collect(Collectors.toList());
    }
}
