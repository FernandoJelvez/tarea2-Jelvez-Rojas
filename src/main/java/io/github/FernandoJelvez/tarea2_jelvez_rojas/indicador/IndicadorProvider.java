package io.github.FernandoJelvez.tarea2_jelvez_rojas.indicador;

import io.github.FernandoJelvez.tarea2_jelvez_rojas.model.Unidad;

import java.time.LocalDate;

public interface IndicadorProvider {
    double valorEnPesos(Unidad unidad, LocalDate fecha);
}
