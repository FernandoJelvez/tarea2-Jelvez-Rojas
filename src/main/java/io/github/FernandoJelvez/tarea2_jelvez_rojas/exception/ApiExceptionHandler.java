package io.github.FernandoJelvez.tarea2_jelvez_rojas.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(UnidadNoValidaException.class)
    public ProblemDetail unidadNoValida(UnidadNoValidaException e){
        return problema(HttpStatus.BAD_REQUEST, "Unidad no valida", e.getMessage());
    }
    @ExceptionHandler(IndicadorNoDisponibleException.class)
    public ProblemDetail indicadorNoDisponible(IndicadorNoDisponibleException e) {
        log.error(e.getMessage(), e);
        return problema(HttpStatus.INTERNAL_SERVER_ERROR, "Indicador no disponible", e.getMessage());
    }
    @ExceptionHandler(ArchivoComprasException.class)
    public ProblemDetail archivoCompras(ArchivoComprasException e) {
        log.error(e.getMessage(), e);
        return problema(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el archivo de compras", e.getMessage());
    }
    private static ProblemDetail problema(HttpStatus status, String titulo, String detalle) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalle);
        return problema;
    }
}
