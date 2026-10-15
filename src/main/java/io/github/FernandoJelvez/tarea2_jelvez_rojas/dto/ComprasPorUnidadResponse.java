package io.github.FernandoJelvez.tarea2_jelvez_rojas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"unidad", "compras", "total_pesos"})
public record ComprasPorUnidadResponse (
        @JsonProperty("unidad") String unidad,
        @JsonProperty("compras") List<CompraEnPesos> compras,
        @JsonProperty("total_pesos") Long totalPesos) {
}
