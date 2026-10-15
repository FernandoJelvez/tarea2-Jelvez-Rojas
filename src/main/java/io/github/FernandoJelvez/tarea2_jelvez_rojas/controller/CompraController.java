package io.github.FernandoJelvez.tarea2_jelvez_rojas.controller;

import io.github.FernandoJelvez.tarea2_jelvez_rojas.service.CompraService;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.dto.ComprasPorUnidadResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/compras")
public class CompraController {
    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }
    @GetMapping("/{unidad}")
    public ComprasPorUnidadResponse comprasPorUnidad(@PathVariable("unidad") String unidad) {
        return compraService.comprasPorUnidad(unidad);
    }
}
