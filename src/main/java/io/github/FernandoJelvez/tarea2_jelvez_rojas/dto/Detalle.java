package io.github.FernandoJelvez.tarea2_jelvez_rojas.dto;

public record Detalle(
        Long id,
        String producto,
        Double monto,
        String unidad,
        String fecha,
        Double valor_unidad,
        Long monto_pesos
) {

}
