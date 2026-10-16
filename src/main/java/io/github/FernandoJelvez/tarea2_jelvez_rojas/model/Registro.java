package io.github.FernandoJelvez.tarea2_jelvez_rojas.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class Registro {
    @JsonProperty("codigo")
    private String codigo;
    @JsonProperty("unidad_medida")
    private String unidad_medida;
    @JsonProperty("serie")
    private Serie[] serie;

    public String getCodigo() {
        return codigo;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public Serie[] getSerie() {
        return serie;
    }
}

