package io.github.FernandoJelvez.tarea2_jelvez_rojas.model;

import java.time.LocalDate;

public record Compra (int id, String producto, double monto, Unidad unidad, LocalDate fecha){
}
