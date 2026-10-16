package io.github.FernandoJelvez.tarea2_jelvez_rojas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","producto","monto","fecha","valor_unidad","monto_pesos"})
public record CompraEnPesos (
        @JsonProperty("id") int id,
        @JsonProperty("producto") String producto,
        @JsonProperty("monto") double monto,
        @JsonProperty("fecha") String fecha,
        @JsonProperty("valor_unidad") double valor_unidad,
        @JsonProperty("monto_pesos") long montoPesos) {
}
