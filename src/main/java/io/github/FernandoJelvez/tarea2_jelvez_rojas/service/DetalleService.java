package io.github.FernandoJelvez.tarea2_jelvez_rojas.service;

import io.github.FernandoJelvez.tarea2_jelvez_rojas.dto.Detalle;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.model.Compra;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.model.Registro;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.util.CsvParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

@Service
public class DetalleService {
    private final HashMap<String, Registro> cacheRegistro = new HashMap<>();
    private final HashMap<Long, Compra> cacheCompra = new HashMap<>();
    CsvParser parser = new CsvParser();
    public Detalle producirDetalle(Long id) throws FileNotFoundException {
        Compra compra;
        if (cacheCompra.containsKey(id)){
            compra = cacheCompra.get(id);
        } else {
            compra = parser.parse(id);
            if (compra==null){
                throw new FileNotFoundException();
            }
            cacheCompra.put(id,compra);
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fecha = compra.getFecha();
        String[] fechaSplit = fecha.split("-");
        LocalDate fechaDate = Date.valueOf(fechaSplit[2]+"-"+fechaSplit[1]+"-"+fechaSplit[0]).toLocalDate();
        int diaDeSemana = fechaDate.getDayOfWeek().getValue();
        if (diaDeSemana>=6) {
            fechaDate = fechaDate.minusDays(8-diaDeSemana);
            fecha = fechaDate.format(dateTimeFormatter);
        }
        String unidad = compra.getUnidad();
        if (Objects.equals(unidad, "USD")){
            unidad="dolar";
        }
        String path = "https://mindicador.cl/api/"+unidad.toLowerCase()+"/"+fecha;
        Registro re;
        if (cacheRegistro.containsKey(path)){
            re = cacheRegistro.get(path);
        } else {
            re = RestClient.create(path).get().accept(MediaType.APPLICATION_JSON).retrieve().body(Registro.class);
            cacheRegistro.put(path,re);
        }
        assert re != null;
        Long pesos = (long) (re.getSerie()[0].valor* compra.getMonto());
        String[] splitFechaCompra = compra.getFecha().split("-");
        String fechaFinal=splitFechaCompra[2]+"-"+splitFechaCompra[1]+"-"+splitFechaCompra[0];
        return new Detalle(id,compra.getProducto(), compra.getMonto(), compra.getUnidad(), fechaFinal, re.getSerie()[0].getValor(),pesos);
    }
}
