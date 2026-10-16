package io.github.FernandoJelvez.tarea2_jelvez_rojas.controller;

import io.github.FernandoJelvez.tarea2_jelvez_rojas.dto.Detalle;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.model.Registro;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.service.DetalleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Stream;

@Controller
@RequestMapping("/")
public class DetalleController {
    private final DetalleService detalleService;

    public DetalleController(DetalleService detalleService) {
        this.detalleService = detalleService;
    }

    @GetMapping("compras/detalle/{id}")
    public ResponseEntity<?> getDetalle(@PathVariable("id") Long id) {
        try {
            Detalle detalle = detalleService.producirDetalle(id);
            return new ResponseEntity<>(detalle, HttpStatus.OK);
        } catch (FileNotFoundException f) {
            return new ResponseEntity<>(Map.of("tipo","http://localhost:8080","fecha", OffsetDateTime.now(), "error","NOT_FOUND","instancia","/compras/detalle/"+id),HttpStatus.NOT_FOUND);
        }
    }
}
