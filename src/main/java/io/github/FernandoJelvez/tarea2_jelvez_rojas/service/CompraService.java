package io.github.FernandoJelvez.tarea2_jelvez_rojas.service;

import io.github.FernandoJelvez.tarea2_jelvez_rojas.dto.CompraEnPesos;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.dto.ComprasPorUnidadResponse;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.exception.UnidadNoValidaException;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.indicador.IndicadorProvider;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.model.Compra;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.model.Unidad;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.repository.CompraRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final IndicadorProvider indicadorProvider;

    public CompraService(CompraRepository compraRepository, IndicadorProvider indicadorProvider) {
        this.compraRepository = compraRepository;
        this.indicadorProvider = indicadorProvider;
    }

    public ComprasPorUnidadResponse comprasPorUnidad(String unidadRuta) {
        Unidad unidad = Unidad.desdeRuta(unidadRuta)
                .orElseThrow(() -> new UnidadNoValidaException(unidadRuta));

        List<CompraEnPesos> compras = compraRepository.findAll().stream()
                .filter(compra -> compra.unidad() == unidad)
                .map(this::convertir)
                .toList();
        long totalPesos = compras.stream()
                .mapToLong(CompraEnPesos::montoPesos)
                .sum();
        return new ComprasPorUnidadResponse(unidad.name(), compras, totalPesos);
    }
    private CompraEnPesos convertir(Compra compra) {
        double valorUnidad = indicadorProvider.valorEnPesos(compra.unidad(), compra.fecha());
        return new CompraEnPesos(
                compra.id(),
                compra.producto(),
                compra.monto(),
                compra.fecha().toString(),
                valorUnidad,
                aPesos(compra.monto(), valorUnidad));
    }
    static long aPesos(double monto, double valorUnidad) {
        return BigDecimal.valueOf(monto)
                .multiply(BigDecimal.valueOf(valorUnidad))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
    }
}

