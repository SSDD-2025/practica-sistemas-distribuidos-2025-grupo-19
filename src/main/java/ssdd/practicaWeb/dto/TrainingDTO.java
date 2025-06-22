package ssdd.practicaWeb.dto;


public record TrainingDTO (
    Long id,
    String name,
    String intensity,
    int duration,
    String exercises,
    String goal,
    Long userId){
}
