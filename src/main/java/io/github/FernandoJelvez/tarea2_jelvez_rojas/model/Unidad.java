package io.github.FernandoJelvez.tarea2_jelvez_rojas.model;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum Unidad {
    UF("uf", "uf"),
    USD("dolar", "dolar");

    private final String ruta;
    private final String codigoMindicador;

    Unidad(String ruta, String codigoMindicador) {
        this.ruta = ruta;
        this.codigoMindicador = codigoMindicador;
    }
    public String ruta() {
        return ruta;
    }
    public String codigoMindicador() {
        return codigoMindicador;
    }
    public static Optional<Unidad> desdeRuta(String valor) {
        if (valor == null) {
            return Optional.empty();
        }
        String normalizado = valor.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(u -> u.ruta.equals(normalizado))
                .findFirst();
    }
    public static Unidad desdeCsv(String valor) {
        String normalizado = valor == null ? "" : valor.trim().toUpperCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(u -> u.name().equals(normalizado))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unidad desconocida en el CSV: " + valor + ""));
    }
}
