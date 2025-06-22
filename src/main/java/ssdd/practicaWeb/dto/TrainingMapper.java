package ssdd.practicaWeb.dto;

import  ssdd.practicaWeb.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    @Mapping(target = "userId", source = "user.id")
    TrainingDTO toDTO(Training training);

    @Mapping(target = "userId", source = "user.id")
    List<TrainingDTO> toDTOs(Collection<Training> trainings);

    @Mapping(target = "user", ignore = true)
    Training toDomain(TrainingDTO trainingDTO);

}
