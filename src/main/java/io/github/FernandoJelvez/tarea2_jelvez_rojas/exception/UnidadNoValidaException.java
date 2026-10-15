package io.github.FernandoJelvez.tarea2_jelvez_rojas.exception;

public class UnidadNoValidaException extends RuntimeException {
    public UnidadNoValidaException(String unidad) {
        super("La unidad ´" + unidad + "´ No existe. Unidades validas: uf, dolar");
    }
}
