package io.github.FernandoJelvez.tarea2_jelvez_rojas.exception;

public class ArchivoComprasException extends RuntimeException {
    public ArchivoComprasException(String mensaje) {
        super(mensaje);
    }
    public ArchivoComprasException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}
