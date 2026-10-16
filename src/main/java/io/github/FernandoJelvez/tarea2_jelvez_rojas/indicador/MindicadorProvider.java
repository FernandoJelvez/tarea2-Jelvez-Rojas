package io.github.FernandoJelvez.tarea2_jelvez_rojas.indicador;

import io.github.FernandoJelvez.tarea2_jelvez_rojas.exception.IndicadorNoDisponibleException;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.model.Unidad;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MindicadorProvider implements IndicadorProvider {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final RestClient restClient;
    private final int maxDiasAtras;
    private final Map<String, Double> cache = new ConcurrentHashMap<>();

    public MindicadorProvider(@Value("${mindicador.base-url}") String baseUrl,
                              @Value("${mindicador.max-dias-atras:10}") int maxDiasAtras) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
        this.maxDiasAtras = maxDiasAtras;
    }
    @Override
    public double valorEnPesos(Unidad unidad, LocalDate fecha) {
        String clave = unidad.name() + "|" + fecha;
        Double enCache = cache.get(clave);
        if (enCache != null) {
            return enCache;
        }
        for (int dias = 0; dias <= this.maxDiasAtras; dias++) {
            Optional<Double> valor = consultar(unidad.codigoMindicador(), fecha.minusDays(dias));
            if (valor.isPresent()) {
                cache.put(clave, valor.get());
                return valor.get();
            }
        }
        throw new IndicadorNoDisponibleException(
                "mindicador.cl no tiene valor de " + unidad.name() + " para " + fecha
                + " ni para los " + maxDiasAtras + " dias anteriores");
    }
    protected  Optional<Double> consultar(String codigoIndicador, LocalDate dia) {
        try {
            MindicadorRespuesta respuesta = restClient.get()
                    .uri("/{indicador}/{fecha}", codigoIndicador, dia.format(FORMATO_FECHA))
                    .retrieve()
                    .body(MindicadorRespuesta.class);

            if (respuesta == null || respuesta.serie() == null || respuesta.serie().isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(respuesta.serie().get(0).valor());
        } catch (RestClientException e) {
            throw new IndicadorNoDisponibleException(
                    "No fue posible consultar a mindicador.cl (" + codigoIndicador + " , " + dia + ")", e);
        }
    }
}
