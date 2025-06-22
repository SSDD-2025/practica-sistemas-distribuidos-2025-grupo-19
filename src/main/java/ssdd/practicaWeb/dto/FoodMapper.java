package ssdd.practicaWeb.dto;

import org.mapstruct.Mapper;
import ssdd.practicaWeb.model.Food;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    FoodDTO toDTO(Food food);

    List<FoodDTO> toDTOs(Collection<Food> foods);

    Food toDomain(FoodDTO foodDTO);
}
