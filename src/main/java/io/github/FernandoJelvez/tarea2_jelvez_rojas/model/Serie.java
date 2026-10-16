package io.github.FernandoJelvez.tarea2_jelvez_rojas.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonAutoDetect
public class Serie {
    @JsonProperty("fecha")
    public OffsetDateTime fecha;
    @JsonProperty("valor")
    public double valor;

    public OffsetDateTime getFecha() {
        return fecha;
    }

    public double getValor() {
        return valor;
    }
}
