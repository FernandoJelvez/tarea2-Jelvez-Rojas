package io.github.FernandoJelvez.tarea2_jelvez_rojas.indicador;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MindicadorRespuesta (String codigo, List<Serie> serie){
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Serie(String fecha, double valor){
    }
}
