package io.github.FernandoJelvez.tarea2_jelvez_rojas.exception;

public class IndicadorNoDisponibleException extends RuntimeException {
    public IndicadorNoDisponibleException(String mensaje) {
        super(mensaje);
    }
    public IndicadorNoDisponibleException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}
